package com.economizai.Processor.Jobs.Processor;

import com.economizai.Processor.Postgres;
import org.springframework.stereotype.Component;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

@Component
public class ProcessRecurrentTransactions {

    public void run() {
        try {
            Connection connection = Postgres.connect();

            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);

            try {
                ResultSet users = statement.executeQuery("SELECT * FROM users");

                while (users.next()) {
                    String userId = users.getString("_id");

                    System.out.println("Processando usuario -> User Id:"+userId);


                    LocalDate currentDate = LocalDate.now();
                    LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
                    LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());

                    String query = "SELECT t.* FROM transactions t WHERE t.user_id = '$1' AND t.is_recurrent = TRUE AND t.movement_date BETWEEN '$2' AND '$3';";
                    String queryConsultTransactions = query
                            .replace("$1", userId)
                            .replace("$2", firstDayOfMonth.toString())
                            .replace("$3", lastDayOfMonth.toString());

                    ResultSet transactions = statement.executeQuery(queryConsultTransactions);
                    
                    while (transactions.next()) {
                        String transactionId = transactions.getString("_id");

                        System.out.println("Checando Transação ID: "+transactionId + " Data de Movimento: "+ transactions.getString("movement_date"));

                        LocalDate movementDate = LocalDate.now();
                        LocalDate firtMovementDate = movementDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                        LocalDate lastMovementDate = movementDate.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

                        String query2 = "SELECT t.* FROM transactions t WHERE t.user_id = '$1' AND t.transaction_reference_id = '$2' AND t.movement_date BETWEEN '$3' AND '$4'";
                        String consultRecurentTransaction = query2
                                .replace("$1",userId)
                                .replace("$2", transactionId)
                                .replace("$3", firtMovementDate.toString())
                                .replace("$4", lastMovementDate.toString());

                        System.out.println(consultRecurentTransaction);

                        ResultSet recurrentTransaction = statement.executeQuery(consultRecurentTransaction);

                        if (!recurrentTransaction.next()) {
                            String query3 = "INSERT INTO public.transactions (\n" +
                                    "  _id,\n" +
                                    "  user_id,\n" +
                                    "  value,\n" +
                                    "  movement_date,\n" +
                                    "  is_installments,\n" +
                                    "  is_recurrent,\n" +
                                    "  bank_account_id,\n" +
                                    "  type, \n" +
                                    "  created_at,\n" +
                                    "  udpated_at,\n" +
                                    "  installments_number,\n" +
                                    "  reference_installments,\n" +
                                    "  category,\n" +
                                    "  card_id,\n" +
                                    "  transaction_reference_id,\n" +
                                    "  has_already_been_paid,\n" +
                                    "  description\n" +
                                    ") VALUES($1, $2, $3, $4, false, true, $5, $6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, '', $7, '', $8, false, 'Movimentação Financeira'::character varying);";
                            String queryCreateRecurrentTransaction = query3
                                    .replace("$1", UUID.randomUUID().toString())
                                    .replace("$2", userId)
                                    .replace("$3", transactions.getString("value"))
                                    .replace("$4", movementDate.plusMonths(1).toString())
                                    .replace("$5", transactions.getString("bank_account_id"))
                                    .replace("$6", transactions.getString("type"))
                                    .replace("$7", transactions.getString("category"))
                                    .replace("$8", transactions.getString("_id"));

                            statement.executeQuery(queryCreateRecurrentTransaction);
                        }
                    }
                }

                connection.rollback();
            }catch(Exception ex) {
                connection.rollback();
            }finally {
                statement.close();
                connection.close();
            }
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
