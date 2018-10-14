package com.knoha.revolut.retransfer;

import com.knoha.revolut.retransfer.api.accounts.BankAccountGetApiController;
import com.knoha.revolut.retransfer.api.transfers.TransferPostApiController;
import com.knoha.revolut.retransfer.db.DatabaseManager;

import static com.knoha.revolut.retransfer.Definitions.Api;
import static com.knoha.revolut.retransfer.Definitions.Type;
import static spark.Spark.get;
import static spark.Spark.post;

public class Application {

    private Application() {
        registerApiRoutes();
    }

    public static void main(String[] args) {
        // Call instance to start DB initialization
        DatabaseManager.getInstance();

        new Application();
    }

    private void registerApiRoutes() {
        get(Api.BankAccount.ID_PATH,
                (req, res) -> new BankAccountGetApiController(req, res).handle());

        post(Api.Transfer.ROOT_PATH, Type.JSON,
                (req, res) -> new TransferPostApiController(req, res).handle());
    }

}
