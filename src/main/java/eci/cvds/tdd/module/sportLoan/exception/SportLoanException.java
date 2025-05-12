package eci.cvds.tdd.module.sportLoan.exception;

public class SportLoanException extends RuntimeException {
    public SportLoanException(String message) {
        super(message);
    }

  // Excepción para cuando el equipo no está disponible
  public static class EquipmentNotAvailableException extends SportLoanException {
    public EquipmentNotAvailableException(String message) {
      super(message);
    }
  }

  // Excepción para cuando el equipo no se encuentra
  public static class EquipmentNotFoundException extends SportLoanException {
    public EquipmentNotFoundException(String message) {
      super(message);
    }
  }
  public static class LoanNotFoundException extends SportLoanException {
    public LoanNotFoundException(String message) {
      super(message);
    }
  }

  public static class LoanAlreadyReturnedException extends SportLoanException {
    public LoanAlreadyReturnedException(String message) {
      super(message);
    }
  }

}
