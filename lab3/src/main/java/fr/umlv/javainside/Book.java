package fr.umlv.javainside;

record Book(@JSONProperty("book-title") String title, int year) { }
