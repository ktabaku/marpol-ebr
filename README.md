# MARPOL eRB

MARPOL eRB is an electronic Oil Record Book, Parts I and II (MARPOL Annex I), built on Eclipse
RCP and JavaFX. The product is `MARPOL-ERB-SERVER`
(`al.sybina.ebr.eclipse-repository/MARPOL-ERB-SERVER.product`). The user manual is
`al.sybina.ebr.rcp/doc/EBR_USER_MANUAL.pdf` (**Help › Help** in the application).

## Try it

Download the file for your system from the project's releases. Each download includes the Java
runtime it needs (Liberica JRE 8 Full, with JavaFX), so nothing else has to be installed.

| System | Download | Start |
|--------|----------|-------|
| Windows 64-bit | `MARPOL-eRB-<version>-windows-x64-setup.exe` (installer) or `MARPOL-eRB-<version>-windows-x64.zip` | Start menu entry, or `MARPOL-ERB-SERVER.exe` in the unzipped `MARPOL-eRB` folder |
| Linux 64-bit | `MARPOL-eRB-<version>-linux-x64.tar.gz` | `tar xzf MARPOL-eRB-<version>-linux-x64.tar.gz`, then `MARPOL-eRB/MARPOL-ERB-SERVER` |
| macOS (Intel, or Apple silicon with Rosetta 2) | `MARPOL-eRB-<version>-macos-x64.tar.gz` | Extract it, then open `MARPOL-eRB.app` |

Extract the Windows zip to a short path such as `C:\MARPOL` or your Downloads folder: some files
are deeply nested and Windows refuses paths longer than 260 characters.

Open one of the two record books on the home screen and log in with user **admin**, password
**admin**. The database already contains an example ship, SYBINA EXAMPLE (IMO 1234567), with fuel
and cargo tanks, so entries can be recorded straight away; change it in the **Marpol detail** tab.

Your data is kept in `MARPOL-ERB-workspace` in your home folder. Delete that folder to start again
from the example ship.

The Windows installer and the macOS application are not signed. On Windows, choose **More info ›
Run anyway** if SmartScreen warns about the installer. On macOS, remove the download quarantine
before the first start:

    xattr -dr com.apple.quarantine MARPOL-eRB.app

## License

MARPOL eRB is licensed under the [Eclipse Public License 2.0](LICENSE). Third-party libraries
keep their own licenses.

## Contents

| Kind | Projects |
|------|----------|
| Runtime plug-ins (21) | `com.opcoach.e4.preferences`, `al.sybina.ebr.commons.domain`, `al.sybina.ebr.commons.utils`, `al.sybina.ebr.jasperreports`, `al.sybina.ebr.javafx.controls`, `al.sybina.ebr.libs`, `al.sybina.ebr.log4j.configuration`, `al.sybina.ebr.logbook.commons`, `al.sybina.ebr.logbook.core`, `al.sybina.ebr.logbook.domain`, `al.sybina.ebr.logbook.ui`, `al.sybina.ebr.logbook2.ui`, `al.sybina.ebr.login`, `al.sybina.ebr.s63`, `al.sybina.ebr.server`, `al.sybina.ebr.plugin.core` (bundle `al.sybina.ebr.core`), `al.sybina.ebr.library.S57Library`, `al.sybina.ebr.rcp`, `org.apache.commons.compress`, `org.eclipse.e4.tools.resources`, `org.osgeo.proj4j` |
| Features (3) | `al.sybina.ebr.rcp.feature`, `al.sybina.ebr.logbook.feature`, `al.sybina.ebr.server.feature` |
| Build / platform | `al.sybina.ebr.eclipse-repository` (product + NSIS installer), `al.sybina.ebr.managment.fx` (target platform `al.sybina.ebr.management.fx.target`), `al.sybina.ebr.p2-site` (local p2 site with third-party bundles) |
| Launch configs | `launches/MARPOL-ERB-SERVER.product.launch` (run the product), `launches/p2_run.launch` (serve the p2 site on port 8080), `launches/p2_site.launch` (rebuild the p2 site), `launches/libs.launch` (regenerate `al.sybina.ebr.libs/lib`), `launches/build.launch` (Maven build of the product), `launches/dist.launch` (the downloads), `launches/manual.launch` (the user manual) |

## Eclipse setup

1. Eclipse 2026-06 RCP with e(fx)clipse, JDK 8 registered as `jdk-1.8` and bound to `JavaSE-1.8`,
   `-Dpde.addTransitiveDependenciesWithForbiddenAccess=false` in `eclipse.ini`.
2. Import every folder of this repository as an existing project (the root folder is the `parent` project).
3. Serve the local p2 site (`launches/p2_run.launch` or `mvn jetty:run` inside `al.sybina.ebr.p2-site`),
   then set `al.sybina.ebr.managment.fx/al.sybina.ebr.management.fx.target` as the target platform.
4. Run `MARPOL-ERB-SERVER.product` from Run Configurations. Runtime data goes to
   `<workspace>/../runtime-MARPOL-ERB-SERVER.product`.

The `pom.xml` error markers on every plug-in come from m2e being unable to run the Tycho 0.25 mojos
in the IDE; they do not affect PDE launches.

## Building the downloads

With the local p2 site running (`launches/p2_run.launch`), build from the root folder with JDK 8:

    mvn clean verify -P dist

or run `launches/dist.launch` in Eclipse. The `dist` profile of `al.sybina.ebr.eclipse-repository`
downloads Liberica JRE 8 Full for each system (the version and checksums are in its `pom.xml`)
and writes the downloads to `al.sybina.ebr.eclipse-repository/target/dist`:

- `MARPOL-eRB-<version>-windows-x64.zip`, `-linux-x64.tar.gz` and `-macos-x64.tar.gz`: the
  product with the runtime in `jre/` (`MARPOL-eRB.app/Contents/Eclipse/jre` on macOS), packaged by
  the descriptors in `src/assembly`, which also set the executable bits for Linux and macOS;
- `MARPOL-eRB-<version>-windows-x64-setup.exe`: the NSIS installer made from
  `nsis/setupMARPOL-ERB-SERVER.nsi`. It is not signed; sign it with `signtool` afterwards if
  needed.

Without `-P dist`, the build only makes the products, without a Java runtime, in `target/products`.

New installations copy the database from `al.sybina.ebr.server/logbookdb/db`, which holds the
example ship.

## Database credentials

The bundled Derby database is encrypted and password protected. Its user, password and encryption
key are in `al.sybina.ebr.commons.utils/src/al/sybina/ebr/commons/utils/database.properties`,
which ships inside the `al.sybina.ebr.commons.utils` bundle and is read by `DatabaseCredentials`:

    database.user=root
    database.password=<password of the database user>
    database.bootPassword=<encryption key of the database>

To use different credentials without changing the bundle, start the product with
`-Dsybina.database.config=<path to a properties file>`. In a properties file, escape `!`, `#`,
`=` and `:` with a backslash. To create a new database with your own values, use
`al.sybina.ebr.logbook.domain/reladomo/logbookInitialData.sql`.
