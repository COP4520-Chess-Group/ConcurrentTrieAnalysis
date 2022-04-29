package types;

public class Move implements Comparable<Move> {
    int from;
    int to;
    private final int piece;
    private final boolean isCheck;
    private final boolean isCheckmate;
    private final boolean isCapture;
    private final CastleType castle;
    private final short promotion;

    private Move(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.piece = builder.piece;
        this.isCheck = builder.isCheck;
        this.isCheckmate = builder.isCheckmate;
        this.isCapture = builder.isCapture;
        this.castle = builder.castle;
        this.promotion = builder.promotion;
    }

    public int getFrom() {
        return this.from;
    }

    public int getTo() {
        return this.to;
    }

    public int getPiece() {
        return piece;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public CastleType isCastle() {
        return castle;
    }

    @Override
    public String toString() {
        String string = Board.pieceToString[piece] + ('a' + this.to % 8) + (this.to/8 + 1);
        switch(this.castle) {
            case KINGSIDE:
                string = "O-O";
                break;
            case QUEENSIDE:
                string = "O-O-O";
                break;
            default:
                break;
        }
        if(this.isCapture) {
            if(this.piece != 9 || this.piece != 1) {
                string = Board.pieceToString[piece] + "x" + ('a' + this.to % 8) + (this.to/8 + 1);
            } else {
                string = ('a' + this.from % 8) + "x" + ('a' + this.to % 8) + (this.to/8 + 1);
            }
        }
        if(this.promotion != 0)
            string += "=" + Board.pieceToString[piece].toUpperCase();
        if(this.isCheckmate) {
            return string + "#";
        } else if(this.isCheck){
            return string + "+";
        }
        return string;
    }

    public static class Builder {
        private int from;
        private int to;
        private int piece;
        private boolean isCheck = false;
        private boolean isCheckmate = false;
        private boolean isCapture = false;
        private CastleType castle = CastleType.NONE;
        private short promotion = 0;

        public Builder(int piece, int from, int to) {
            this.piece = piece;
            this.from = from;
            this.to = to;
        }

        public Builder isCheck(boolean check) {
            this.isCheck = check;
            return this;
        }

        public Builder isCheckmate(boolean checkmate) {
            this.isCheckmate = checkmate;
            return this;
        }

        public Builder isCapture(boolean capture) {
            this.isCapture = capture;
            return this;
        }

        public Builder castle(CastleType castle) {
            this.castle = castle;
            return this;
        }

        public Builder promotion(short promotion)
        {
            this.promotion = promotion;
            return this;
        }

        public Move build() {
            return new Move(this);
        }
    }

    public enum CastleType {
        NONE, QUEENSIDE, KINGSIDE;
    }

    @Override
    public int compareTo(Move o) {
        int comp;
        comp = this.to % 8 - o.to % 8;
        comp += this.from % 8 - o.from % 8;
        return comp > 0 ? 1 : comp < 0 ? -1 : 0;
    }
}
