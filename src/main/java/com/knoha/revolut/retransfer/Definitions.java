package com.knoha.revolut.retransfer;

public final class Definitions {

    private Definitions() {
        throw new UnsupportedOperationException();
    }

    public static final class Api {

        private static final String API_ROOT = "/rt/api";

        private Api() {
            throw new UnsupportedOperationException();
        }

        public static final class BankAccount {

            public static final String ROOT_PATH = API_ROOT + "/bank-accounts";

            public static final String ACCOUNT_ID_PARAM_NAME = "accountId";

            public static final String ID_PATH = ROOT_PATH + "/:" + ACCOUNT_ID_PARAM_NAME;

            private BankAccount() {
                throw new UnsupportedOperationException();
            }
        }

        public static final class Transfer {

            public static final String ROOT_PATH = API_ROOT + "/transfer";

            private Transfer() {
                throw new UnsupportedOperationException();
            }
        }
    }

    public static final class Type {

        public static String JSON = "application/json";

        private Type() {
            throw new UnsupportedOperationException();
        }
    }

}
