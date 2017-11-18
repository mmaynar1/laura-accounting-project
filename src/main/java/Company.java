import java.math.BigDecimal;
import java.math.RoundingMode;

public class Company
{
   private String name;
   private BigDecimal assets;
   private BigDecimal liabilities;
   private BigDecimal equities;
   private BigDecimal incomes;

   public Company( String name )
   {
      this.name = name;
      assets = BigDecimal.ZERO;
      liabilities = BigDecimal.ZERO;
      equities = BigDecimal.ZERO;
      incomes = BigDecimal.ZERO;
   }

   public void addAccountType( AccountType accountType, BigDecimal accountTotal )
   {
      switch( accountType )
      {
         case Assets:
            addAsset( accountTotal );
            break;
         case Liabilities:
            addLiability( accountTotal );
            break;
         case Equity:
            addEquity( accountTotal );
            break;
         case Income:
            addIncome( accountTotal );
            break;
         default:
            throw new RuntimeException("Invalid account type");
      }
   }

   public void print()
   {
      System.out.println( "----------" + getName() + "----------" );
      System.out.println( round( getAssets() ) );
      System.out.println( round( getLiabilities() ) );
      System.out.println( round( getEquities() ) );
      System.out.println( round( getIncomes() ) );
      System.out.println( round( getTotal() ) );
   }

   private BigDecimal round( BigDecimal value )
   {
      return value.setScale( 2, RoundingMode.HALF_UP );
   }

   private void addAsset( BigDecimal asset )
   {
      assets = assets.add( asset );
   }

   private void addLiability( BigDecimal liability )
   {
      liabilities = liabilities.add( liability );
   }

   private void addEquity( BigDecimal equity )
   {
      equities = equities.add( equity );
   }

   private void addIncome( BigDecimal income )
   {
      incomes = incomes.add( income );
   }

   public BigDecimal getTotal()
   {
      return getAssets().add( getLiabilities() ).add( getEquities() ).add( getIncomes() );
   }

   public String getName()
   {
      return name;
   }

   public BigDecimal getAssets()
   {
      return assets;
   }

   public BigDecimal getLiabilities()
   {
      return liabilities;
   }

   public BigDecimal getEquities()
   {
      return equities;
   }

   public BigDecimal getIncomes()
   {
      return incomes;
   }
}
