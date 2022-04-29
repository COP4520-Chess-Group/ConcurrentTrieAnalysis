package types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Board implements Iterable<Move>, Comparable<Board> {

    public static final int[] fileToNum = {1, 2, 3, 4, 5, 6, 7, 8};
    public static final String[] pieceToString = {null, "P", "R", "N", "B", "Q", "K", null, null, "p", "r", "n", "b", "q", "k"};
    private String castlingRights = "KQkq";
    private short toMove = 0b0000;
    private String enPassant = "";
    private int halfMoveClock = 0;
    private int fullMoveClock = 0;
    private int[] board = {
        0x23456432,
        0x11111111,
        0x0,
        0x0,
        0x0,
        0x0,
        0x99999999,
        0xABCDECBA    
    };

    private ArrayList<Move> history = new ArrayList<>();

    public ArrayList<Move> generateMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        for(int rank = 0; rank < 8; rank++) {
            for(int file = 0; file < 8; file++) {
                int piece = ((board[rank] & (0xF << (file)*4)) >>> file*4);
                if((piece & toMove) == 0)
                    continue;
                Move m = null;
                int square = rank*8+file;
                switch(piece & 0b111) {
                    case 0b001: // pawn
                        m = new Move.Builder(piece, square, square + (toMove == 0 ? 9 : -9)).build();
                        if(isLegal(m))
                            moves.add(m);
                        m = new Move.Builder(piece, square, square + (toMove == 0 ? 7 : -7)).build();
                        if(isLegal(m))
                            moves.add(m);
                        break;
                    case 0b010: // rook
                        break;
                    case 0b011: // knight
                        int[] knightMoves = {-17, -15, -10, -6, 6, 10, 15, 17};
                    case 0b100: // bishop
                    case 0b101: // queen
                    case 0b111: // king
                }
                if(isLegal(m))
                    moves.add(m);
            }
        }
        return moves;
    }

    public void move(Move m) {
        this.history.add(m);
        int rank = m.getFrom() / 8;
        int file = m.getFrom() % 8;
        board[rank] &= ~(0xF << file*4); // clear the square the piece is leaving
        rank = m.getTo() / 8;
        file = m.getTo() % 8;
        board[rank] &= ~(0xF << file*4); // clear the square the piece is going to
        board[rank] |= ((m.getPiece() & 0xF) << file*4); // set the square
        toMove ^= 0b1000;
    }

    /**
     * Checks if the current board position is legal, essentially, if it is the opponent's turn
     * and the opposite king is in check, then the board position is illegal
     * @param m The move to check is legal
     * @return True if the position is legal
     */
    public boolean isLegal(Move m) {
        int[] prev = Arrays.copyOf(board, 8);
        int rank = m.getFrom() / 8;
        int file = m.getFrom() % 8;
        prev[rank] &= ~(0xF << file*4); // clear the square the piece is leaving
        rank = m.getTo() / 8;
        file = m.getTo() % 8;
        prev[rank] &= ~(0xF << file*4); // clear the square the piece is going to
        prev[rank] |= ((m.getPiece() & 0xF) << file*4); // set the square
        int king = findKing(toMove);
        /* Knights
         *  Knight moves are +15, +17, +10, -6, -15, -17, -10, +6
         * Pawns
         *  Pawns move diagonally forward 1 space to attack that is +7 +9 or their negatives
         */
        int[] knightMoves = {-17, -15, -10, -6, 6, 10, 15, 17};
        int mask = toMove | 0b0011;
        for(int delta : knightMoves) {
            int square = king + delta;
            int piece = ((prev[square/8] & (0xF << (square % 8)*4)) >>> (square % 8)*4);
            if(piece == mask)
                return false;
        }
        int[] pawnMoves = {toMove == 0 ? 9 : -9, toMove == 0 ? 7 : -7};
        mask = toMove | 0b0001;
        for(int delta : pawnMoves) {
            int square = king + delta;
            int piece = ((prev[square/8] & (0xF << (square % 8)*4)) >>> (square % 8)*4);
            if(piece == mask)
                return false;
        }
        // Diagonals
        int[] diagonals = {7, 9, -7, 9};
        for(int i = 1; i <= 8; i++) {
            for(int diag : diagonals) {
                int square = king + diag*i;
                if(square > 64) continue;
                if(square < 0) continue;
                int piece = ((prev[square/8] & (0xF << (square % 8)*4)) >>> (square % 8)*4);
                if(piece == (toMove |0b0101) || piece == (toMove | 0b0100))
                    return false;
            }
        }
        // Straight lines
        int[] straightLines = {-8, -1, 1, 8};
        for(int i = 1; i <= 8; i++) {
            for(int straight : straightLines) {
                int square = king + straight*i;
                if(square > 64) continue;
                if(square < 0) continue;
                int piece = ((prev[square/8] & (0xF << (square % 8)*4)) >>> (square % 8)*4);
                if(piece == (toMove |0b0101) || piece == (toMove | 0b0010))
                    return false;
            }
        }
        return true;
    }

    /**
     * 
     * @param color The king's color we are trying to find
     * @return The number of the square that king is on
     */
    private int findKing(int color) {
        int mask = (color | 0b0110);
        for(int rank = 0; rank < 8; rank++) {
            for(int file = 0; file < 8; file++)
                if(((board[rank] & (0xF << file*4)) >>> file*4) == mask)
                    return rank*8+file;
        }
        return 0;
    }

    @Override
    public int compareTo(Board o) {
        Iterator<Move> moves = this.iterator();
        Iterator<Move> comp = o.iterator();
        while(true) {
            if(!moves.hasNext() && comp.hasNext())
                return -1;
            if(!comp.hasNext() && moves.hasNext())
                return 1;
            else if(!moves.hasNext() && !comp.hasNext())
                return 0;
            Move mine = moves.next();
            Move vs = comp.next();
            if(mine.compareTo(vs) != 0)
                return mine.compareTo(vs);
        }
    }

    @Override
    public Iterator<Move> iterator() {
        return this.history.iterator();
    }
    
    /**
     * @return FEN for the current board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int rank = 7; rank >= 0; rank--) {
            int blanks = 0;
            for(int file = 0; file < 8; file++) {
                if((board[rank] & (0xF << file*4)) == 0)
                    blanks++;
                else {
                    if(blanks != 0) {
                        sb.append(blanks);
                        blanks = 0;
                    }
                    int num = (board[rank] & (0xF << file*4)) >> file*4;
                    String piece = pieceToString[num & 0xF];
                    sb.append(piece);
                }
            }
            if(blanks != 0)
                sb.append(blanks);
            if(rank != 0) {
                sb.append("/");
            }
        }
        sb.append(String.format(" %c %s %s %d %d",
                toMove == 0b0000 ? 'w' : 'b',
                this.castlingRights,
                this.enPassant,
                this.halfMoveClock,
                this.fullMoveClock));
        return sb.toString();
    }

}
