package org.example;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class Main {
    public static void main(final String[] args) {
        WeldContainer container = new Weld().initialize();
        try {
        } finally {
            container.shutdown();
        }
    }
}