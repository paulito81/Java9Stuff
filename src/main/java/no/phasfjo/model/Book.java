package no.phasfjo.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Book {
    private String title;
    private Set<String> authors;

    public static void setBookList(List<Book> bookList) {
        Book.bookList = bookList;
    }

    private static List<Book> bookList;
    private double price;

    public Book(String title, Set<String> authors, double price) {
        this.title = title;
        this.authors = authors;
        this.price = price;
    }

    public Book() {
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public static Book newBook(String title, Set<String> authors, double price){
        Book b =  new Book(title, authors, price);
        bookList = new ArrayList<>();
        bookList.add(b);
        return b;
    }

    public static List<Book> getBooks() {
        return Objects.requireNonNullElseGet(bookList, ArrayList::new);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", price=" + price +
                '}';
    }
}
