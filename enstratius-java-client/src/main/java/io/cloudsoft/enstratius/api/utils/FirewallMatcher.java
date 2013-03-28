package io.cloudsoft.enstratius.api.utils;

import com.google.common.base.Predicate;
import io.cloudsoft.enstratius.api.model.Firewall;

public class FirewallMatcher implements Predicate<Firewall> {

   private final String name;

   public FirewallMatcher(String name) {
         this.name = name;
      }

      @Override
      public boolean apply(Firewall firewall) {
         return firewall.getName().equalsIgnoreCase(name);
      }
}
