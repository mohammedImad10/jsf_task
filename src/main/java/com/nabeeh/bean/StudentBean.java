package com.nabeeh.bean;

import com.nabeeh.dao.StudentDAO;
import com.nabeeh.model.Student;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

@ManagedBean(name = "studentBean")
@ViewScoped
public class StudentBean {

    private Student student = new Student();
    private List<Student> students;
    private StudentDAO studentDAO = new StudentDAO();

    public List<Student> getStudents() {
        students = studentDAO.getAllStudents();
        return students;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void addStudent() {
        if (validateStudent()) {
            studentDAO.addStudent(student);
            student = new Student(); // Reset the form
        }
    }

    public void updateStudent() {
        if (validateStudent()) {
            studentDAO.updateStudent(student);
            student = new Student(); // Reset the form
        }
    }

    private boolean validateStudent() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Student> violation : violations) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, violation.getMessage(), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            return false;
        }
        return true;
    }

    public void editStudent(Student student) {
        this.student = student;
    }

    public void deleteStudent(int id) {
        studentDAO.deleteStudent(id);
    }
}
