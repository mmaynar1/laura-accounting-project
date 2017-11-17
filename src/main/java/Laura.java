import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Laura
{
   public static final int COLUMN_M = 12;

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
      List<Company> companies = new ArrayList<Company>();
      for ( int i = 0; i < workbook.getNumberOfSheets(); i++ )
      {
         Sheet sheet = workbook.getSheetAt( i );
         Company company = new Company( sheet.getSheetName() );
         for ( Row row : sheet )
         {
            populateCompany( evaluator, company, row );
         }
         companies.add( company );
      }

      print( companies );
   }

   private void print( List<Company> companies )
   {
      for ( Company company : companies )
      {
         System.out.println( "----------" + company.getName() + "----------" );
         System.out.println( round( company.getAssets() ) );
         System.out.println( round( company.getLiabilities() ) );
         System.out.println( round( company.getEquities() ) );
         System.out.println( round( company.getIncomes() ) );
         System.out.println( round( company.getTotal() ) );
      }
   }

   private void populateCompany( FormulaEvaluator evaluator, Company company, Row row )
   {
      for ( Cell cell : row )
      {
         CellReference cellReference = new CellReference( row.getRowNum(), cell.getColumnIndex() );
         if ( cellReference.formatAsString().contains( "A" ) && cell.getCellType() == 0 )
         {
            String accountNumber = (int) cell.getNumericCellValue() + "";

            if ( AccountType.get( accountNumber ) == AccountType.Assets )
            {
               BigDecimal accountTotal = getBigDecimal( evaluator, row );
               company.addAsset( accountTotal );
            }
            else if ( AccountType.get( accountNumber ) == AccountType.Liabilities )
            {
               BigDecimal accountTotal = getBigDecimal( evaluator, row );
               company.addLiability( accountTotal );
            }
            else if ( AccountType.get( accountNumber ) == AccountType.Equity )
            {
               BigDecimal accountTotal = getBigDecimal( evaluator, row );
               company.addEquity( accountTotal );
            }
            else if ( AccountType.get( accountNumber ) == AccountType.Income )
            {
               BigDecimal accountTotal = getBigDecimal( evaluator, row );
               company.addIncome( accountTotal );
            }

         }
      }
   }

   private BigDecimal round( BigDecimal value )
   {
      return value.setScale( 2, RoundingMode.HALF_UP );
   }

   private BigDecimal getBigDecimal( FormulaEvaluator evaluator, Row row )
   {
      return new BigDecimal( evaluator.evaluate( row.getCell( COLUMN_M ) ).formatAsString() );
   }
}