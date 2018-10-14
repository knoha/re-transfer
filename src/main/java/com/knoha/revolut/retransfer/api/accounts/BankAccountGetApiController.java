package com.knoha.revolut.retransfer.api.accounts;

import com.knoha.revolut.retransfer.Definitions;
import com.knoha.revolut.retransfer.api.BaseApiController;
import com.knoha.revolut.retransfer.models.accounts.BankAccount;
import com.knoha.revolut.retransfer.services.accounts.base.BankAccountServiceImpl;
import com.knoha.revolut.retransfer.services.accounts.spi.BankAccountService;
import spark.Request;
import spark.Response;

public class BankAccountGetApiController extends BaseApiController {

    private BankAccountService bankAccountService = new BankAccountServiceImpl();

    public BankAccountGetApiController(final Request request, final Response response) {
        super(request, response);
    }

    @Override
    public Object handle() {
        final String accountId = getRequest().params(Definitions.Api.BankAccount.ACCOUNT_ID_PARAM_NAME);
        if (accountId == null || accountId.length() == 0) {
            return badRequest("Account identifier is required.");
        }

        final BankAccount account = bankAccountService.retrieve(Long.parseLong(accountId));
        if (account == null) {
            return notFound("Account does not exist.");
        }

        return ok(account);
    }
}
