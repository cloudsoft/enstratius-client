package io.cloudsoft.enstratius.api.utils;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.Uninterruptibles;
import io.cloudsoft.enstratius.api.features.AdminApi;
import io.cloudsoft.enstratius.api.model.Job;
import io.cloudsoft.enstratius.api.model.Status;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Jobs {

   public static Job waitForJob(Job job, AdminApi adminApi) throws IOException {
      return waitForJob(job, 5 * 60, adminApi);
   }

   public static Job waitForJob(Job job, int maxWaitInSec, AdminApi adminApi) throws IOException {
      Stopwatch stopWatch = new Stopwatch();
      stopWatch.start();
      while (job.getStatus() != Status.COMPLETE && stopWatch.elapsedTime(TimeUnit.SECONDS) < maxWaitInSec) {
         Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
         job = adminApi.getJob(job.getJobId());
         if (job.getStatus() == Status.ERROR)
            return job;
      }
      return job;
   }

   public static boolean isComplete(Job job) {
      return job.getStatus() == Status.COMPLETE;
   }
}
