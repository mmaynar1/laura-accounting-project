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
   public void isCute( String filePath )
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

   }

   private List<Company> processCompanies(Workbook workbook, FormulaEvaluator evaluator )
   {
      List<Company> companies = new ArrayList<Company>();
      for ( int i = 0; i < workbook.getNumberOfSheets(); i++ )
      {
         Sheet sheet = workbook.getSheetAt( i );
         String companyName = sheet.getSheetName();

         if( companyName.toUpperCase().contains("COMPANY"))
         {
            Company company = new Company( companyName );
            for (Row row : sheet)
            {
               populateCompany(evaluator, company, row);
            }
            companies.add( company );
         }

      }
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
            String accountNumber = getAccountNumber(cell);
            AccountType accountType = AccountType.get( accountNumber );
            BigDecimal accountTotal = getAccountTotal( evaluator, row );
            company.addAccountType( accountType, accountTotal );
         }
      }
   }

   private String getAccountNumber(Cell cell)
   {
      String accountNumber = "";
      if( cell.getCellType() == Cell.CELL_TYPE_STRING)
      {
         accountNumber = cell.getStringCellValue();
      }
      else if( cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
      {
         accountNumber = (int) cell.getNumericCellValue() + "";
      }
      else
      {
         throw new RuntimeException("Cell type for cell " + new CellReference( cell ).formatAsString() + ": " + cell.getCellType());
      }
      return accountNumber;
   }

   private boolean isAccount( Cell cell)
   {
      final int accountColumn = 1; //Column B in Excel
      final int beginningRow = 3;
      return cell.getRow().getRowNum() >= beginningRow && cell.getColumnIndex() == accountColumn && cell.getCellType() != Cell.CELL_TYPE_BLANK;
   }

   private BigDecimal getAccountTotal(FormulaEvaluator evaluator, Row row )
   {
      final int totalColumn = 15; //Column P in Excel
      try
      {
         return new BigDecimal(evaluator.evaluate(row.getCell(totalColumn)).formatAsString()).setScale(2, BigDecimal.ROUND_HALF_UP);
      }
      catch( Exception exception )
      {
         System.out.println( "Exception thrown on cell: " + new CellReference( row.getCell(totalColumn) ));
         throw exception;
      }
   }
}