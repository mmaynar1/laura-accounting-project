import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Laura
{

   public static final String COMPANY = "COMPANY";

   public void isCute(String filePath )
   {
      try
      {
         System.out.println( System.getProperty( "user.dir" ) );
         Workbook workbook = WorkbookFactory.create( new FileInputStream( filePath ) );
         FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
         workbook.setForceFormulaRecalculation( true );
         processWorkbook( workbook, evaluator );

      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }
   }

   private void processWorkbook( Workbook workbook, FormulaEvaluator evaluator )
   {
      List<Company> companies = processCompanies(workbook, evaluator);
      List<Company> bi360Companies = getBI360Companies( workbook, evaluator, companies );
      System.out.println("----------BI360 Companies----------");
      for( Company company : bi360Companies )
      {
         company.print();
      }
      List<Company> differences = getDifferences( companies, bi360Companies);
      System.out.println("----------Differences----------");
      for( Company difference : differences )
      {
         difference.print();
      }
   }

   private List<Company> getDifferences(List<Company> companies, List<Company> bi360Companies)
   {
      List<Company> differences = new ArrayList<>();

      for (int i = 0; i < companies.size(); i++)
      {
         Company company = companies.get(i);
         Company bi360company = bi360Companies.get(i);
         Company difference = new Company( company.getName());

         difference.addAccountType(AccountType.Assets, bi360company.getAssets().subtract(company.getAssets()));
         difference.addAccountType(AccountType.Liabilities, bi360company.getLiabilities().subtract(company.getLiabilities()));
         difference.addAccountType(AccountType.Equity, bi360company.getEquities().subtract(company.getEquities()));
         difference.addAccountType(AccountType.Income, bi360company.getIncomes().subtract(company.getIncomes()));

         differences.add(difference);
      }
      return differences;
   }

   private List<Company> getBI360Companies(Workbook workbook, FormulaEvaluator evaluator, List<Company> companies)
   {
      List<Company> bi360companies = initializeBI360Companies(companies);

      for ( int i = 0; i < workbook.getNumberOfSheets(); i++ )
      {
         Sheet sheet = workbook.getSheetAt(i);
         String companyName = sheet.getSheetName();
         if( !companyName.toUpperCase().contains(COMPANY))
         {
            for (Row row : sheet)
            {
               for( Cell cell : row )
               {
                  if( isCompany( cell ) )
                  {
                     String companyNumber = getValue( cell );
                     AccountType accountType = AccountType.getFromString(getValue( row.getCell( cell.getColumnIndex() + 1 ) ).trim());
                     BigDecimal accountTotal = getValue( evaluator, row, cell.getColumnIndex() + 3 );
                     Company bi360Company = getCompany( companyNumber, bi360companies );
                     bi360Company.addAccountType(accountType, accountTotal);
                  }
               }
            }
         }

      }
      return bi360companies;
   }

   private static String removeLeading(String source, String characters)
   {
      if (null == source)
      {
         source = "";
      }
      if (null == characters)
      {
         characters = "";
      }

      while (source.length() > 0)
      {
         String firstCharacter = source.substring(0, 1);
         if (characters.contains(firstCharacter))
         {
            if (source.length() > 1)
            {
               source = source.substring(1);
            }
            else
            {
               source = "";
            }
         }
         else
         {
            break;
         }
      }
      return source;
   }

   private Company getCompany(String companyNumber, List<Company> bi360companies)
   {
      Company company = null;
      for( Company bi360company : bi360companies )
      {
         if( removeLeading(bi360company.getNumber(), "0" ).equals(removeLeading(companyNumber, "0")) )
         {
            company = bi360company;
         }
      }

      return company;
   }

   private List<Company> initializeBI360Companies(List<Company> companies)
   {
      List<Company> bi360companies = new ArrayList<>();
      for( Company company : companies )
      {
         bi360companies.add( new Company( company.getName() ));
      }
      return bi360companies;
   }

   private boolean isCompany( Cell cell)
   {
      final int accountColumn = 0; //Column A in Excel
      final int beginningRow = 3;
      return cell.getRow().getRowNum() >= beginningRow && cell.getColumnIndex() == accountColumn && cell.getCellType() != Cell.CELL_TYPE_BLANK;
   }

   private List<Company> processCompanies(Workbook workbook, FormulaEvaluator evaluator )
   {
      List<Company> companies = new ArrayList<Company>();
      for ( int i = 0; i < workbook.getNumberOfSheets(); i++ )
      {
         Sheet sheet = workbook.getSheetAt( i );
         String companyName = sheet.getSheetName();

         if( companyName.toUpperCase().contains(COMPANY))
         {
            Company company = new Company( companyName );
            for (Row row : sheet)
            {
               populateCompany(evaluator, company, row);
            }
            companies.add( company );
         }

      }
      System.out.println("----------Companies----------");
      print( companies );
      return companies;
   }

   private void print( List<Company> companies )
   {
      for ( Company company : companies )
      {
         company.print();
      }
   }

   private void populateCompany( FormulaEvaluator evaluator, Company company, Row row )
   {
      for ( Cell cell : row )
      {
         if ( isAccount( cell ) )
         {
            String accountNumber = getValue(cell);
            AccountType accountType = AccountType.get( accountNumber );
            final int totalColumn = 15; //Column P in Excel
            BigDecimal accountTotal = getValue( evaluator, row, totalColumn );
            company.addAccountType( accountType, accountTotal );
         }
      }
   }

   private String getValue(Cell cell)
   {
      String value = "";
      if( cell.getCellType() == Cell.CELL_TYPE_STRING)
      {
         value = cell.getStringCellValue();
      }
      else if( cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
      {
         value = (int) cell.getNumericCellValue() + "";
      }
      else
      {
         throw new RuntimeException("Cell type for cell " + new CellReference( cell ).formatAsString() + ": " + cell.getCellType());
      }
      return value;
   }

   private boolean isAccount( Cell cell)
   {
      final int accountColumn = 1; //Column B in Excel
      final int beginningRow = 3;
      return cell.getRow().getRowNum() >= beginningRow && cell.getColumnIndex() == accountColumn && cell.getCellType() != Cell.CELL_TYPE_BLANK;
   }

   private BigDecimal getValue(FormulaEvaluator evaluator, Row row, int column )
   {
      try
      {
         return new BigDecimal(evaluator.evaluate(row.getCell(column)).formatAsString()).setScale(2, BigDecimal.ROUND_HALF_UP);
      }
      catch( Exception exception )
      {
         System.out.println( "Exception thrown on cell: " + new CellReference( row.getCell(column) ));
         throw exception;
      }
   }
}