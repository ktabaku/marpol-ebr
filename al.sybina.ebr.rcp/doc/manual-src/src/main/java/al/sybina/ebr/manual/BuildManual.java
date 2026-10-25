package al.sybina.ebr.manual;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

/**
 * Builds the MARPOL eRB user manual, the PDF that Help &gt; Help opens.
 * <p>
 * Usage: {@code BuildManual <images folder> <output pdf>}. The text of the manual is in
 * {@link #content()}; the screenshots are read from the images folder.
 */
public final class BuildManual {

	private static final Color NAVY = new Color(11, 37, 69);
	private static final Color DEEP = new Color(19, 64, 104);
	private static final Color TEAL = new Color(28, 167, 200);
	private static final Color GREY = new Color(94, 112, 133);
	private static final Color TEXT = new Color(27, 39, 51);
	private static final Color RULE = new Color(201, 211, 221);

	private static final Pattern MARKUP = Pattern.compile("\\*\\*(.+?)\\*\\*|__(.+?)__");

	private final File images;
	private final Document document;
	private final PdfWriter writer;
	private final BaseFont regular;
	private final BaseFont bold;
	private final BaseFont italic;
	private PdfOutline chapterOutline;
	private int anchor;

	private BuildManual(File images, File output) throws IOException, DocumentException {
		this.images = images;
		regular = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		bold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		italic = BaseFont.createFont(BaseFont.HELVETICA_OBLIQUE, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		document = new Document(PageSize.A4, 54, 54, 54, 60);
		writer = PdfWriter.getInstance(document, new FileOutputStream(output));
		writer.setStrictImageSequence(true);
		writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
		writer.setPageEvent(new Footer());
		document.addTitle("MARPOL eRB User Manual");
		document.addAuthor("Sybina");
		document.addSubject("Electronic Oil Record Book, Parts I and II");
		document.addCreator("Sybina");
		document.open();
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: BuildManual <images folder> <output pdf>");
			System.exit(2);
		}
		File output = new File(args[1]);
		BuildManual manual = new BuildManual(new File(args[0]), output);
		manual.content();
		manual.document.close();
		System.out.println("written " + output.getAbsolutePath());
	}

	// ------------------------------------------------------------------ content

	private static final String[][] PART1 = {
		{ "A", "Ballasting or cleaning of oil fuel tanks" },
		{ "B", "Discharge of dirty ballast or cleaning water from oil fuel tanks referred to under section A" },
		{ "C", "Collection, transfer and disposal of oil residues (sludge)" },
		{ "D", "Non-automatic starting of discharge overboard, transfer or disposal otherwise of bilge water which has accumulated in machinery spaces" },
		{ "E", "Automatic starting of discharge overboard, transfer or disposal otherwise of bilge water which has accumulated in machinery spaces" },
		{ "F", "Condition of the oil filtering equipment" },
		{ "G", "Accidental or other exceptional discharges of oil" },
		{ "H", "Bunkering of fuel or bulk lubricating oil" },
		{ "I", "Additional operational procedures and general remarks" },
	};

	private static final String[][] PART2 = {
		{ "A", "Loading of oil cargo" },
		{ "B", "Internal transfer of oil cargo during voyage" },
		{ "C", "Unloading of oil cargo" },
		{ "D", "Crude oil washing (COW tankers only)" },
		{ "E", "Ballasting of cargo tanks" },
		{ "F", "Ballasting of dedicated clean ballast tanks (CBT tankers only)" },
		{ "G", "Cleaning of cargo tanks" },
		{ "H", "Discharge of dirty ballast" },
		{ "I", "Discharge of water from slop tanks into the sea" },
		{ "J", "Collection, transfer and disposal of residues and oily mixtures not otherwise dealt with" },
		{ "K", "Discharge of clean ballast contained in cargo tanks" },
		{ "L", "Discharge of ballast from dedicated clean ballast tanks (CBT tankers only)" },
		{ "M", "Condition of oil discharge monitoring and control system" },
		{ "N", "Accidental or other exceptional discharges of oil" },
		{ "O", "Additional operational procedures and general remarks" },
		{ "P", "Loading of ballast water (tankers engaged in specific trades)" },
		{ "Q", "Re-allocation of ballast water within the ship (tankers engaged in specific trades)" },
		{ "R", "Ballast water discharge to reception facility" },
	};

	private void content() throws DocumentException, IOException {
		cover();

		h1("1. Introduction");
		p("MARPOL eRB is an electronic Oil Record Book for ships, following MARPOL Annex I. It keeps:");
		bullets("**Oil Record Book Part I**: machinery space operations, for all ships.",
				"**Oil Record Book Part II**: cargo and ballast operations, for oil tankers.");
		p("Each entry is recorded under its MARPOL code letter, signed by the officer in charge and endorsed by "
				+ "the master. The application keeps the full history of entries and corrections, an audit log of user "
				+ "activity, and daily backups of its database.");

		h1("2. Getting started");
		h2("2.1 Home screen");
		p("After start-up the application opens the home screen. The two buttons on the right open the two record books.");
		figure("home.jpg", "Home screen", 100);
		h2("2.2 Logging in");
		p("Opening a record book asks for your user name and password. Type them and press **LOGIN** or Enter. Your "
				+ "name is then shown at the top right, next to **Logout**. Pages that require the Chief Engineer's "
				+ "signature are endorsed by the master's signature.");
		figure("login.jpg", "Login dialog", 45);
		p("Use **Logout** at the top right to end your session. The login dialog appears again.");
		h2("2.3 Switching between the record books");
		p("Use **Perspectives › Show Perspective** to switch between the **Main Perspective** (home screen), "
				+ "**Oil Record Book Part I** and **Oil Record Book Part 2**.");

		h1("3. Setting up the ship (Marpol detail)");
		p("Before any entry can be saved or signed, the ship and its tanks must be set up. Open the **Marpol detail** "
				+ "tab of either record book.");
		figure("marpol_detail.jpg", "Marpol detail: identification of tanks", 75);
		bullets("**Ship Particulars**: name, distinctive number and other particulars of the ship. These are printed on every report.",
				"**Identification of tanks**: cargo, slop and ballast tanks, fuel tanks, oil and bilge tanks, sludge and other "
						+ "tanks, and incinerators and boilers. For each group, add tanks on the port side, centre and starboard "
						+ "side with **Add Tanks**, and enter their identifier and volume in m³. The total volume of each "
						+ "group is calculated for you.");
		p("Press **Save** to store the details, or **Return ORB** to go back to the record book. The tanks you define "
				+ "here are the ones offered in the code letter forms.");
		figure("ship_particulars.jpg", "Marpol detail: ship particulars", 75);

		h1("4. Recording an entry");
		figure("part1.jpg", "Oil Record Book Part I, code letter A", 85);
		numbered("Pick the **DATE** of the operation with the calendar button. The date cannot be in the future, and "
						+ "cannot be before the **Last entry in the oil record book** shown on the right.",
				"Select the tab of the **code letter** of the operation.",
				"Tick the check box of each item you want to record. This enables its fields. Fill in the fields; use the "
						+ "**+** buttons to choose tanks and **ADD OPERATION** where an operation can repeat.",
				"Press **PREVIEW** to check the entry as it will appear in the record book.",
				"Press **SIGNATURE**, enter your **Rank**, **Name** and **Surname**, sign in the signature area and press "
						+ "**ACCEPT**. **RESET** clears the signature.",
				"Press **SAVE** to record the entry. **CLEAR ALL** empties the form instead.");
		p("The application checks every entry before it is previewed, signed or saved, and tells you what is missing "
				+ "or inconsistent (for example a missing date, tank or quantity).");
		h2("4.1 Guidance for each code letter");
		p("The orange **i** buttons open guidance for the code letter or item: what has to be recorded, a filled-in "
				+ "example, the resulting record book entry and notes on how to measure quantities.");
		figure("info.jpg", "Guidance for Part II code letter A", 55);
		h2("4.2 Oil Record Book Part I code letters");
		codeLetters(PART1);

		h2("4.3 Oil Record Book Part II code letters");
		figure("part2.jpg", "Oil Record Book Part II, code letter A", 100);
		codeLetters(PART2);

		document.newPage();
		h1("5. History");
		p("The **History** tab lists the recorded entries with their date, code letter, item number, content, correction "
				+ "note, the name and signature of the officer, and the master's signature. Filter the list by **Ship "
				+ "Operations Date** and by **Log Registration Date**; the **Actual Ship Operations** and **Actual Log "
				+ "Registration** options limit it to the current entries.");
		bullets("**History Preview** and **Preview** produce the record book pages as a report.",
				"**Master Sign & Save** lets the master endorse the selected entries.");
		figure("history.jpg", "History", 100);

		document.newPage();
		h1("6. Users");
		p("Administrators manage the users in the **Users** tab. To add a user, choose a role and fill in user name, "
				+ "password, first and last name, date of birth and e-mail, then press **Save**; **Reset** clears the "
				+ "form. Use the edit button of a row to change an existing user. Only users with the right permission "
				+ "can create administrators.");
		figure("users.jpg", "Users", 100);

		h1("7. Audit logging");
		p("The **Audit Logging** tab shows the audit log: who logged in and when, changes to the logbook settings, and "
				+ "the automatic database backups.");

		h1("8. Data backup and restore");
		p("The database is backed up automatically every day at 23:00 UTC into the __backup__ folder of the "
				+ "installation. You can also back up and restore by hand with **File › Data Backup/Restore**:");
		bullets("**Data Backup**: choose the format (**Standard** zip archive or **Xml**), browse for the folder to write "
						+ "the backup to, and press **OK**.",
				"**Data Restore**: choose the format, browse for the backup file, and press **OK**. Restoring replaces the "
						+ "current data with the content of the backup.");
		figure("backup.jpg", "Data Backup/Restore", 50);
		p("**File › Restart** restarts the application and **File › Exit** closes it.");

		h1("9. Preferences");
		p("Open **Window › Preferences**.");
		bullets("**Multi Installations**: IP address, port and database location of the main installation. Workstations "
						+ "connected to a main installation use its database.",
				"**License**: the user permit, the machine identifier file and the license file. Use **Create System "
						+ "Identifier File** to produce the identifier needed to request a license.");
		figure("prefs_install.jpg", "Preferences: main installation", 70);

		h1("10. Themes, help and version");
		bullets("**Theme**: choose the **Light**, **Default** or **Modern** theme.",
				"**Help › Help** opens this manual.",
				"**Help › About** shows the version and release date.",
				"**Help › Check for system updates** looks for updates on the update server.");
		figure("about.jpg", "About", 55);

		h1("11. Messages and what to do");
		message("Date error: Please specify the compilation date.",
				"Pick the date of the operation with the calendar button.");
		message("Signature Required: Please sign the modifications.",
				"Press SIGNATURE, fill in rank, name and surname, sign and press ACCEPT.");
		message("Ship not configured.",
				"Enter the ship in Marpol detail › Ship Particulars and save, then try again.");
		message("Alert code Nr …", "Complete or correct the item named in the message.");
		figure("ship_warning.jpg", "Message shown when no ship has been set up", 45);
	}

	// ------------------------------------------------------------------ building blocks

	private Font font(BaseFont base, float size, Color color) {
		return new Font(base, size, Font.NORMAL, color);
	}

	private void cover() throws DocumentException {
		Paragraph spacer = new Paragraph(" ");
		spacer.setSpacingBefore(170);
		document.add(spacer);
		centered("S Y B I N A", font(regular, 13, TEAL), 0);
		centered("MARPOL eRB", font(bold, 34, NAVY), 14);
		centered("Electronic Record Book for the Oil Record Book, Parts I and II", font(regular, 14, DEEP), 18);
		centered("User Manual", font(regular, 14, DEEP), 6);
		centered("Version 1.0.0 · October 2026", font(regular, 10, GREY), 40);
		document.newPage();
	}

	private void centered(String text, Font f, float before) throws DocumentException {
		Paragraph p = new Paragraph(text, f);
		p.setAlignment(Element.ALIGN_CENTER);
		p.setSpacingBefore(before);
		document.add(p);
	}

	/** Starts a new page when less than {@code space} points are left, so a heading is not left alone at the bottom. */
	private void keepWithNext(float space) {
		if (writer.getVerticalPosition(false) - document.bottom() < space) {
			document.newPage();
		}
	}

	private void h1(String title) throws DocumentException {
		keepWithNext(150);
		String name = "h" + (++anchor);
		Paragraph p = new Paragraph(new Chunk(title, font(bold, 17, NAVY)).setLocalDestination(name));
		p.setSpacingBefore(18);
		p.setSpacingAfter(2);
		document.add(p);
		LineSeparator line = new LineSeparator(0.8f, 100, TEAL, Element.ALIGN_LEFT, -2);
		document.add(new Chunk(line));
		chapterOutline = new PdfOutline(writer.getDirectContent().getRootOutline(),
				PdfAction.gotoLocalPage(name, false), title);
	}

	private void h2(String title) throws DocumentException {
		keepWithNext(120);
		String name = "h" + (++anchor);
		Paragraph p = new Paragraph(new Chunk(title, font(bold, 12.5f, DEEP)).setLocalDestination(name));
		p.setSpacingBefore(12);
		p.setSpacingAfter(4);
		document.add(p);
		new PdfOutline(chapterOutline, PdfAction.gotoLocalPage(name, false), title);
	}

	/** Text with **bold** and __italic__ runs. */
	private Phrase rich(String text, float size) {
		Phrase phrase = new Phrase();
		phrase.setLeading(size * 1.35f);
		Matcher m = MARKUP.matcher(text);
		int pos = 0;
		while (m.find()) {
			phrase.add(new Chunk(text.substring(pos, m.start()), font(regular, size, TEXT)));
			if (m.group(1) != null) {
				phrase.add(new Chunk(m.group(1), font(bold, size, TEXT)));
			} else {
				phrase.add(new Chunk(m.group(2), font(italic, size, TEXT)));
			}
			pos = m.end();
		}
		phrase.add(new Chunk(text.substring(pos), font(regular, size, TEXT)));
		return phrase;
	}

	private void p(String text) throws DocumentException {
		Paragraph p = new Paragraph(rich(text, 10.5f));
		p.setSpacingBefore(3);
		p.setSpacingAfter(5);
		document.add(p);
	}

	private void bullets(String... items) throws DocumentException {
		List list = new List(List.UNORDERED, 14);
		list.setListSymbol(new Chunk("•", font(regular, 10.5f, TEXT)));
		addItems(list, items);
	}

	private void numbered(String... items) throws DocumentException {
		List list = new List(List.ORDERED, 16);
		addItems(list, items);
	}

	private void addItems(List list, String... items) throws DocumentException {
		list.setIndentationLeft(12);
		for (String item : items) {
			ListItem li = new ListItem(rich(item, 10.5f));
			li.setSpacingAfter(2);
			list.add(li);
		}
		Paragraph wrapper = new Paragraph();
		wrapper.setSpacingAfter(6);
		wrapper.add(list);
		document.add(wrapper);
	}

	private void codeLetters(String[][] letters) throws DocumentException {
		for (int i = 0; i < letters.length; i++) {
			if (i > 0) {
				Paragraph rule = new Paragraph(new Chunk(new LineSeparator(0.5f, 100, RULE, Element.ALIGN_LEFT, 3)));
				rule.setLeading(4);
				document.add(rule);
			}
			Paragraph p = new Paragraph();
			p.setLeading(13);
			p.setAlignment(Element.ALIGN_LEFT);
			p.add(new Chunk(letters[i][0] + ".  ", font(bold, 9.5f, TEXT)));
			p.add(new Chunk(letters[i][1], font(regular, 9.5f, TEXT)));
			p.setSpacingBefore(i > 0 ? 3 : 2);
			document.add(p);
		}
		Paragraph after = new Paragraph(" ");
		after.setLeading(8);
		document.add(after);
	}

	private void message(String text, String action) throws DocumentException {
		Paragraph p = new Paragraph();
		p.setLeading(14);
		p.add(new Chunk(text, font(bold, 10.5f, TEXT)));
		p.add(Chunk.NEWLINE);
		p.add(new Chunk(action, font(regular, 10.5f, TEXT)));
		p.setSpacingAfter(6);
		document.add(p);
	}

	/** A screenshot, scaled to the given percentage of the text width, with a caption. */
	private void figure(String file, String caption, float widthPercent) throws DocumentException, IOException {
		Image image = Image.getInstance(new File(images, file).getAbsolutePath());
		float width = (document.right() - document.left()) * widthPercent / 100f;
		image.scaleToFit(width, document.top() - document.bottom() - 40);
		image.setAlignment(Image.MIDDLE);
		image.setSpacingBefore(6);
		document.add(image);
		Paragraph cap = new Paragraph(caption, font(regular, 8.5f, GREY));
		cap.setAlignment(Element.ALIGN_CENTER);
		cap.setSpacingAfter(8);
		document.add(cap);
	}

	/** "MARPOL eRB | page n" at the bottom of every page except the cover. */
	private final class Footer extends PdfPageEventHelper {
		@Override
		public void onEndPage(PdfWriter w, Document d) {
			if (w.getPageNumber() == 1) {
				return;
			}
			ColumnText.showTextAligned(w.getDirectContent(), Element.ALIGN_CENTER,
					new Phrase("MARPOL eRB  |  page " + w.getPageNumber(), font(regular, 8, GREY)),
					(d.right() + d.left()) / 2, d.bottom() - 30, 0);
		}
	}
}
