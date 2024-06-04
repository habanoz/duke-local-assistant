package com.habanoz.duke.config;

import java.util.ArrayList;
import java.util.List;

// Define the superclass
class Superclass {
    // Superclass properties and methods
}

// Define the subclasses
class Subclass1 extends Superclass {
    // Subclass1 properties and methods
}

class Subclass2 extends Superclass {
    // Subclass2 properties and methods
}

public class Test {
    public static void main(String[] args) {
        // Create a list that can hold subtypes of Superclass
        List<Superclass> superclassList = new ArrayList<>();

        // Create instances of subclasses
        Subclass1 subclass1 = new Subclass1();
        Subclass2 subclass2 = new Subclass2();

        // Add subclass instances to the list
        superclassList.add(subclass1);
        superclassList.add(subclass2);

        // Access and use the objects in the list
        for (Superclass item : superclassList) {
            // You can access common properties and methods of Superclass here
            System.out.println(item); // Access and use the objects
        }
    }
}