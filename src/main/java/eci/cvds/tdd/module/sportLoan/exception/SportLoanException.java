package eci.cvds.tdd.module.sportLoan.exception;

/**
 * Excepción base para el sistema de préstamos deportivos.
 * Se utiliza para manejar errores relacionados con la gestión de préstamos y equipos.
 */
public class SportLoanException extends RuntimeException {

  /**
   * Crea una nueva instancia de excepción con un mensaje descriptivo.
   *
   * @param message Mensaje que describe la causa del error.
   */
    public SportLoanException(String message) {
        super(message);
    }

  /**
   * Excepción lanzada cuando un equipo no está disponible para préstamo.
   */
  public static class EquipmentNotAvailableException extends SportLoanException {
    public EquipmentNotAvailableException(String message) {
      super(message);
    }
  }

  /**
   * Excepción lanzada cuando un equipo no se encuentra en el sistema.
   */
  public static class EquipmentNotFoundException extends SportLoanException {
    public EquipmentNotFoundException(String message) {
      super(message);
    }
  }

  /**
   * Excepción lanzada cuando un préstamo no se encuentra en el sistema.
   */
  public static class LoanNotFoundException extends SportLoanException {
    public LoanNotFoundException(String message) {
      super(message);
    }
  }

  /**
   * Excepción lanzada cuando un usuario no se encuentra en el sistema.
   */
  public static class UserNotFoundException extends SportLoanException {
    public UserNotFoundException(String message) {
      super(message);
    }
  }

  /**
   * Excepción lanzada cuando un préstamo ya ha sido devuelto y se intenta devolver nuevamente.
   */
  public static class LoanAlreadyReturnedException extends SportLoanException {
    public LoanAlreadyReturnedException(String message) {
      super(message);
    }
  }
}
