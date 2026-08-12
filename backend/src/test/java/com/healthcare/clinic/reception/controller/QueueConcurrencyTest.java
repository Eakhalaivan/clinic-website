package com.healthcare.clinic.reception.controller;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.repository.BranchRepository;
import com.healthcare.clinic.reception.entity.QueueToken;
import com.healthcare.clinic.reception.service.QueueTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class QueueConcurrencyTest {

    @Autowired
    private QueueTokenService queueTokenService;

    @Autowired
    private BranchRepository branchRepository;

    @Test
    public void testConcurrentTokenGeneration() throws InterruptedException {
        // Setup
        Branch branch = new Branch();
        branch.setName("Concurrent Test Branch");
        branch.setCity("Test City");
        branch.setCountry("Test Country");
        branch.setTimezone("UTC");
        branch.setAddress("Test Address");
        branch.setEmail("test@branch.com");
        branch.setPhoneNumber("1234567890");
        branch.setPostalCode("12345");
        branch.setState("Test State");
        branch = branchRepository.save(branch);

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(1);
        List<Future<QueueToken>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            Branch finalBranch = branch;
            futures.add(executorService.submit(() -> {
                latch.await(); // wait until all threads are ready
                return queueTokenService.generateToken(finalBranch, null, null);
            }));
        }

        latch.countDown(); // release all threads at once

        List<QueueToken> tokens = new ArrayList<>();
        for (Future<QueueToken> future : futures) {
            try {
                tokens.add(future.get());
            } catch (ExecutionException e) {
                // If it fails, we will catch it here, but it shouldn't if retries work
                e.printStackTrace();
            }
        }

        assertEquals(numberOfThreads, tokens.size(), "Should have generated tokens for all threads");

        Set<Integer> uniqueTokenNumbers = tokens.stream().map(QueueToken::getTokenNumber).collect(Collectors.toSet());
        assertEquals(numberOfThreads, uniqueTokenNumbers.size(), "Token numbers should be unique");
    }
}
