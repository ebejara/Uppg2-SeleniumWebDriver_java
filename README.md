# Uppg2 - Selenium WebDriver med Java

Detta är inlämningsuppgift 2 i kursen Continuous Integration (CI).  
Projektet använder **Selenium WebDriver** i **Java** med **JUnit 5** och **Maven** för att automatisera tester mot webbplatsen [saucedemo.com](https://www.saucedemo.com/).

## Uppgiftens krav

**G-nivå (Grundläggande):**
- Ett testfall för lyckad inloggning med korrekta uppgifter
- Verifiering att användaren hamnar på inventory-sidan efter inloggning

### Utökade tester för VG
- Test med felaktigt användarnamn → rätt felmeddelande visas
- Test med felaktigt lösenord → rätt felmeddelande visas

## Teknikstack

- **Språk**: Java 17
- **Testramverk**: JUnit 5
- **Web-automation**: Selenium WebDriver 4.27.0
- **Driver-hantering**: WebDriverManager (automatisk nedladdning av ChromeDriver)
- **Byggverktyg**: Maven
- **Logging**: SLF4J + Logback
- **IDE**: Visual Studio Code

## Förutsättningar

- Java 17 installerat
- Maven installerat (eller använd VS Code's inbyggda Maven-stöd)
- Google Chrome installerat

## Installation & Körning lokalt

1. Klona repot:
   ```bash
   git clone https://github.com/ebejara/Uppg2-SeleniumWebDriver_java.git
   cd Uppg2-SeleniumWebDriver_java
