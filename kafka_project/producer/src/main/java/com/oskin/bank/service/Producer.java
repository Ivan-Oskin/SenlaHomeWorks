package com.oskin.bank.service;

import com.oskin.bank.model.Account;
import com.oskin.bank.model.Transfer;
import com.oskin.bank.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Producer {
    private Map<Integer, Account> accountMap = new LinkedHashMap<>();
    private int firstKey = 1;
    private KafkaTemplate<String, Transfer> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(Producer.class);
    Random random = new Random();
    AccountRepository accountRepository;

    @Autowired
    public Producer(AccountRepository accountRepository, KafkaTemplate<String, Transfer> kafkaTemplate) {
        this.accountRepository = accountRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void run() {
        if (accountRepository.count() == 0) {
            accountRepository.resetSequenceToOne();
            for (int i = 1; i <= 1000; i++) {
                int rand_int = random.nextInt(4001) + 1000;
                Account account = new Account(rand_int);
                accountMap.put(i, account);
            }
            accountRepository.saveAll(accountMap.values());
        } else {
            accountMap = accountRepository.findAll()
                    .stream()
                    .collect(Collectors.toMap(Account::getId, account -> account));
            firstKey = accountMap.keySet().stream().iterator().next();
        }
    }

    @Scheduled(fixedDelay = 200)
    public void sendMessage() {
        logger.info("начало отправки сообщения");
        int firstId = random.nextInt(1001) + firstKey;
        int secondId = random.nextInt(1001) + firstKey;
        int randSum = random.nextInt(4001) + 1000;
        Transfer transfer = new Transfer(firstId, secondId, randSum);
        String partitionKey = String.valueOf(firstId % 3);
        kafkaTemplate.send("bank.transfers", partitionKey, transfer);
        logger.info("конец отправки сообщения");
    }
}
