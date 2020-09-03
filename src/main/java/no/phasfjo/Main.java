package no.phasfjo;

import no.phasfjo.model.Book;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;
import java.awt.image.MultiResolutionImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws Exception {
        runAll();
    }

    private static void runAll() throws Exception{
        bookStuff();
        streamsOfInts();
        moreBookStuff();
        optionalBookStuff();
        multipleOptionalBook();
        printProcessHandlerUnsorted();
        printProcessHandlerSorted();
        destroyTextEditProcess();
        simpleHttp();
        asyncHttp();
        taskBarDemo();
        imagesDemo();
        destroyTextEditProcess();
    }

    private static void bookStuff(){
        long zero = Stream.ofNullable(null).count();
        long one = Stream.ofNullable(getBook()).count();
        System.out.printf("Zero: %d, One: %d", zero, one);
    }

    @org.jetbrains.annotations.NotNull
    private static Book getBook() {
        return Book.newBook("Lord of The Rings", Collections.singleton("Tolkien"), 300.0);
    }

    private static void streamsOfInts(){
        System.out.println("-----INT DEMO-----");
        Stream<Optional<Integer>> optional = Stream.of(Optional.of(1), Optional.empty(), Optional.of(2));
        Stream<Integer> ints = optional.flatMap(Optional::stream);
        ints.forEach(System.out::println);
    }

    private static void moreBookStuff(){
        System.out.println("-----BOOK DEMO------");
        Book.newBook("Lord of The Rings", Collections.singleton("Tolkien"), 300.0);
        Set<String> auth = Book.getBooks().stream().map(book -> book.getAuthors().stream().findFirst()).flatMap(Optional::stream).collect(Collectors.toSet());
        auth.forEach(System.out::println);
    }
    private static void optionalBookStuff(){
        Book b = new Book("The Old Man and the Sea", Collections.singleton("Ernest Hemingway"), 200);
        Book c = new Book();
        List<Book> books = new ArrayList<>();
        books.add(b);
        books.add(c);
        Book.setBookList(books);
        Optional<Book> full = Optional.of(c);
        full.ifPresent(System.out::println);

        if (full.isPresent()) {
            System.out.println("Is present ");
        } else {
            System.err.println("Nothing here");
        }

        full.ifPresentOrElse(System.out::println, () -> System.out.println("Nothing here"));
        Optional.empty().ifPresentOrElse(System.out::println, () -> System.out.println("Nothing here"));
    }

    private static void multipleOptionalBook(){
        Book b1 = new Book("The Old Man and the Sea", Collections.singleton("Ernest Hemingway"), 200);
        Optional<Book> localFallback = Optional.of(b1);

        Book bestBookBefore = getBestOffer().orElse(getExternalOffer().orElse(localFallback.get()));

        Optional<Book> bestBook = getBestOffer().or(Main::getExternalOffer).or(() -> localFallback);
        System.out.println(bestBook);
    }

    static Optional<Book> getBestOffer(){
        return Optional.empty();
    }

    static Optional<Book> getExternalOffer(){
        return Optional.of(new Book("External Book", Set.of(), 11.99));
    }

    private static void printProcessHandlerUnsorted(){
        ProcessHandle.allProcesses()
                .map(ProcessHandle::info)
                .forEach(Processing::printInfo);
    }

    private static void printProcessHandlerSorted(){
        ProcessHandle.allProcesses()
                .map(ProcessHandle::info)
                .sorted(Comparator.comparing(info ->
                        info.startInstant().orElse(Instant.MAX)))
                .forEach(Processing::printInfo);
    }

    private static void destroyTextEditProcess(){
        ProcessHandle textEditHandle =
                ProcessHandle.allProcesses()
                .filter(h -> h.info().commandLine().map(cmd -> cmd.contains("TextEdit"))
                .orElse(false))
                .findFirst()
                .orElse(ProcessHandle.current());

        System.out.println(textEditHandle.info());
        String f = textEditHandle.info().commandLine().map(cmd -> cmd.contains("TextEdit")).toString();

        if(f.contains("false")){
            System.out.println(f);
            return;
        }
        textEditHandle.onExit().thenAccept(h -> System.out.println("TextEdit was killed by Java"));
        boolean shutdown = textEditHandle.destroy();
        textEditHandle.onExit().join();
        System.out.println("Shutdown: " + shutdown);
    }

    private static void simpleHttp() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://openjdk.java.net/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200){
            System.out.println(response.headers().map());
        }
    }
    private static void asyncHttp() {
         HttpClient.Builder builder = HttpClient.newBuilder();
         builder.version(HttpClient.Version.HTTP_2).followRedirects(HttpClient.Redirect.ALWAYS);
         HttpClient client = builder.build();

         HttpRequest request = HttpRequest.newBuilder(URI.create("https://www.google.com"))
                 .header("User-Agent", "Java")
                 .timeout(Duration.ofMillis(500))
                 .GET()
                 .build();

       CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
       
       response.join(); // wait until termination
       response.thenAccept(r -> {
            System.out.println("Version: " + r.version());
            System.out.println(r.body());
        });
    }

    private static void taskBarDemo() {
        try {
            new JavaTaskBar();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void imagesDemo() throws IOException {

       MultiResolutionImage multiResolutionImage = null;
       Image lowres = null;
       Image highres = null;
       if(loadImages()!= null)
       multiResolutionImage = loadImages();
       if(multiResolutionImage != null)
        lowres = multiResolutionImage.getResolutionVariant(50,50);
        if(multiResolutionImage != null)
            highres = multiResolutionImage.getResolutionVariant(200, 200);
       if(lowres != null)
       System.out.printf("Width for 50: %d%n", lowres.getWidth(null) );
       if(highres != null)
       System.out.printf("Width for 200: %d%n", highres.getWidth(null) );
    }

    private static MultiResolutionImage loadImages() throws IOException {
        BufferedImage img = null;
        String PATH = "src/images/";
        Image[] images = null;
        try {
            img = ImageIO.read(new File(PATH + "sander.tiff"));
            images = new Image[]{
                    ImageIO.read(new File(PATH + "sander_lowres.png")),
                    ImageIO.read(new File(PATH + "sander.png"))
            };
        }catch (Exception e){
            e.printStackTrace();
        }


//        if(img !=null) {
//            ImageReader tiffReader = ImageIO.getImageReadersByFormatName("tiff").next();
//            tiffReader.setInput(inputStream);
//            BufferedImage image = tiffReader.read(0);
//        }
        return images !=null ?  new BaseMultiResolutionImage(images) : null;
     }

}
