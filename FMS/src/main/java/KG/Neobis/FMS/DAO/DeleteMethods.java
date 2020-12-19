package KG.Neobis.FMS.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeleteMethods {

    private final JdbcTemplate jdbcTemplate;

    public DeleteMethods(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteContractor(Long contractorId){
        String sql ="update transactions set contractor = null where contractor = ?";
        jdbcTemplate.update(sql,contractorId);
    }

    public void deleteExpenseCategory(Long categoryID){
        String sql = "update transactions set expenses_category = null where expenses_category = ?";
        jdbcTemplate.update(sql,categoryID);
    }

    public void deleteIncomeCategory(Long categoryID){
        String sql = "update transactions set incomes_category = null where incomes_category = ?";
        jdbcTemplate.update(sql,categoryID);
    }

    public void deleteProject(Long categoryID){
        String sql = "update transactions set projects = null where projects = ?";
        jdbcTemplate.update(sql,categoryID);
    }

    public void deleteRole(Long roleID){
        String sql = "delete from app_users_roles where roles_id = ?";
        jdbcTemplate.update(sql,roleID);
    }
}
