# User manual sources

`BuildManual` (Java 8, iText 2.1.7) generates `../EBR_USER_MANUAL.pdf`, the manual that
**Help › Help** opens. The text is in `src/main/java/al/sybina/ebr/manual/BuildManual.java`;
the screenshots are in `images/`.

## Regenerate the PDF

In Eclipse, run the `manual` launch configuration (`launches/manual.launch`), or from this folder:

```
mvn package
```

## Update a screenshot

Replace the JPEG in `images/` with a new capture of the running product, keeping the file name,
and regenerate. Screenshots are scaled to the page width, so captures of the full window
(about 1100 to 1400 pixels wide) work best.

This folder is not packaged with the product; the rcp bundle's `build.properties` only includes
`doc/EBR_USER_MANUAL.pdf`.
