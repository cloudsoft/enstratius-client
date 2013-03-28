package io.cloudsoft.enstratius.api.utils;

import com.google.common.base.Predicate;
import io.cloudsoft.enstratius.api.model.MachineImage;

public class MachineImageMatcher implements Predicate<MachineImage> {

   private final String providerId;

   public MachineImageMatcher(String providerId) {
         this.providerId = providerId;
      }

      @Override
      public boolean apply(MachineImage machineImage) {
         return machineImage.getProviderId().equalsIgnoreCase(providerId);
      }
}
