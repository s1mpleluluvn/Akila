package com.akila.redis.lock;

public enum LockKey {
    // Lock key for buy ticket
    COMBO_BUY_TICKET,
    ADD_REWARD_ACCOUNT,
    // Lock key for calculate winning result
    LOAD_WINNING_RESULT_FOR_CALCULATE,
    // Lock key for calculate winning bingo
    LOAD_WINNING_RESULT_AUTO_BINGO,
    // Lock key for winning payment command
    CREATE_WINNING_PAYMENT_COMMAND,
    // Lock key for approve winning payment command batch
    APPROVE_WINNING_PAYMENT_COMMAND_BATCH,
    // Lock key for send winning result sms
    LOAD_SMS_FOR_SEND_WINNING_RESULT,
    // create refund request vinatti
    CREATE_REFUND_REQUEST_VINATTI,

    //Add/delete Agent Bank Account
    AGENT_MODIFY_BANK_ACCOUNT
}
