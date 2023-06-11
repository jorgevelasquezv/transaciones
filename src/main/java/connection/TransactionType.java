package connection;

import java.util.Arrays;

public enum TransactionType {
    RESPONSE(0){
        @Override
        public String getType() {
            return "RESPONSE";
        }
    },

    INSERT_EMPLOYED(1){
        @Override
        public String getType() {
            return "INSERT_EMPLOYED";
        }
    },
    UPDATE_EMPLOYED(2){
        @Override
        public String getType() {
            return "UPDATE_EMPLOYED";
        }
    },
    SELECT_ONE_EMPLOYED(3){
        @Override
        public String getType() {
            return "SELECT_EMPLOYED";
        }
    },
    SELECT_ALL_EMPLOYED(4){
        @Override
        public String getType() {
            return "SELECT_ALL_EMPLOYED";
        }
    },
    DELETE_EMPLOYED(5){
        @Override
        public String getType() {
            return "DELETE_EMPLOYED";
        }
    },
    INSERT_COUNTRY(6){
        @Override
        public String getType(){ return "INSERT_COUNTRY";}
    },
    INSERT_CITY(7){
        @Override
        public String getType(){ return "INSERT_CITY";}
    },
    INSERT_LOCALIZATION(8){
        @Override
        public String getType(){ return "INSERT_LOCALIZATION";}
    },
    INSERT_DEPARTMENT(9){
        @Override
        public String getType(){ return "INSERT_DEPARTMENT";}
    },
    INSERT_POSITION(10){
        @Override
        public String getType(){ return "INSERT_POSITION";}
    },

    SELECT_POSITIONS(11){
        @Override
        public String getType(){ return "SELECT_POSITIONS";}
    },

    SELECT_DEPARTMENTS(12){
        @Override
        public String getType(){ return "SELECT_DEPARTMENTS";}
    },

    SELECT_CITIES(13){
        @Override
        public String getType(){ return "SELECT_CITIES";}
    },

    SELECT_COUNTRIES(14){
        @Override
        public String getType(){ return "SELECT_COUNTRIES";}
    },

    SELECT_LOCALIZATIONS(15){
        @Override
        public String getType(){ return "SELECT_LOCALIZATIONS";}
    },

    SELECT_MANAGERS(16){
        @Override
        public String getType(){ return "SELECT_MANAGERS";}
    },

    SELECT_ALL_EMPLOYED_RETIREMENT(17){
        @Override
        public String getType(){ return "SELECT_MANAGERS";}
    };

    private int type;

    private TransactionType(Integer type) {
        this.type = type;
    }

    public abstract String getType();

    public void setType(int type) {
        this.type = type;
    }

    static public TransactionType getType(int type){
        return (TransactionType) Arrays.stream(TransactionType.values()).filter(transaction -> transaction.type == type).findFirst().get();
    }

}
