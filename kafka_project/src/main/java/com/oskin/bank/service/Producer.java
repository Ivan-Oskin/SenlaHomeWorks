package com.oskin.bank.service;

import com.oskin.bank.model.Account;
import com.oskin.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class Producer {
    private Map<Integer, Account> accountMap = new HashMap<>();
    Random random = new Random();
    AccountRepository accountRepository;

    @Autowired
    public Producer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void run() {
        if (accountRepository.count() == 0) {
            accountRepository.resetSequenceToOne();
            for (int i = 1; i <= 1000; i++) {
                int rand_int = random.nextInt(4001)+1000;
                Account account = new Account(rand_int);
                accountMap.put(i,account);
            }
            accountRepository.saveAll(accountMap.values());
        } else {
            accountMap = accountRepository.findAll()
                    .stream()
                    .collect(Collectors.toMap(Account::getId, account -> account));
        }
    }
}
