package com.oskin.bank.service;

import com.oskin.bank.model.Account;
import com.oskin.bank.model.Transfer;
import com.oskin.bank.model.TransferStatus;
import com.oskin.bank.repository.AccountRepository;
import com.oskin.bank.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class Consumer {
    Logger logger = LoggerFactory.getLogger(Consumer.class);
    AccountRepository accountRepository;
    TransferRepository transferRepository;

    @Autowired
    public Consumer(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    public void run() {

    }

    @KafkaListener(
            topics = "bank.transfers",
            groupId = "bank-consumer-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void listen(Transfer transfer, Acknowledgment ack) {
        try {
            logger.info("Получено сообщение: {}", transfer);
            Optional<Account> first = accountRepository.findById(transfer.getIdWithdrawing());
            Optional<Account> second = accountRepository.findById(transfer.getIdDepositing());
            if (first.isPresent() && second.isPresent()) {
                Account accountFirst = first.get();
                Account accountSecond = second.get();
                if(accountFirst.getBalance() >= transfer.getSum()) {
                    accountFirst.setBalance(accountFirst.getBalance()-transfer.getSum());
                    accountSecond.setBalance(accountSecond.getBalance()+transfer.getSum());
                    transfer.setStatus(TransferStatus.SUCCESSFULLY);
                    accountRepository.save(accountFirst);
                    accountRepository.save(accountSecond);
                    transferRepository.save(transfer);
                    logger.info("Успешно!");
                } else {
                    transfer.setStatus(TransferStatus.ERROR);
                    transferRepository.save(transfer);
                    logger.info("Завершилось с ошибкой");
                }
            } else {
                logger.info("Ошибка! Какой то из счетов не существует");
            }
            ack.acknowledge();
        } catch (Exception e) {
            logger.error("Ошибка при обработке перевода: {}", e.getMessage());
        }
    }
}
