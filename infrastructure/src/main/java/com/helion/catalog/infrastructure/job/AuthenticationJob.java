package com.helion.catalog.infrastructure.job;

import com.helion.catalog.infrastructure.authentication.RefreshClientCredentials;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuthenticationJob {

    public final RefreshClientCredentials refreshClientCredentials;

    public AuthenticationJob(RefreshClientCredentials refreshClientCredentials) {
        this.refreshClientCredentials = Objects.requireNonNull(refreshClientCredentials);
    }

    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES, initialDelay = 3)
    public void refreshClientCredentials(){
        this.refreshClientCredentials.refresh();
    }
}
