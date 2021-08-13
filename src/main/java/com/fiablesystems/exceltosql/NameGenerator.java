package com.fiablesystems.exceltosql;

import com.github.javafaker.Faker;

import java.util.stream.Stream;

public class NameGenerator {
    public static void main(String[] args) {
        Faker faker=new Faker();
        Stream.iterate(1,count -> count+1).limit(25).forEach(e->{
            System.out.println(faker.name().lastName());
        });
    }
}
