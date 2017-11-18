public enum AccountType
{
   Assets( "1" ),
   Liabilities( "2" ),
   Equity( "3" ),
   Income( "other" );

   private String code;

   AccountType( String code )
   {
      this.code = code;
   }

   public static AccountType getFromString(String name)
   {
      AccountType type = null;
      if (name.toUpperCase().contains("ASSET"))
      {
         type = Assets;
      }
      else if (name.toUpperCase().contains("LIABILIT"))
      {
         type = Liabilities;
      }
      else if (name.toUpperCase().contains("EQUIT"))
      {
         type = Equity;
      }
      else if (name.toUpperCase().contains("INCOME"))
      {
         type = Income;
      }
      return type;
   }

   public static AccountType get( String code )
   {
      code = code.charAt( 0 ) + "";
      AccountType type = null;
      for ( AccountType accountType : values() )
      {
         if ( code.equals( accountType.getCode() ) )
         {
            type = accountType;
         }
      }

      if ( type == null )
      {
         type = Income;
      }

      return type;
   }

   public String getCode()
   {
      return code;
   }
}
