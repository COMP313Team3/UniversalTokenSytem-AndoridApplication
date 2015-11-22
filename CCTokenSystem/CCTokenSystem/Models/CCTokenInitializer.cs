using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CCTokenSystem.Models
{
    public class CCTokenInitializer : DropCreateDatabaseIfModelChanges<CCTokenSystemContext>
    {

        //seed mehtod is used to insert data into table while it created
        protected override void Seed(CCTokenSystemContext context)
        {
            var student = new List<Student>{
                new Student{
                    StudentID=300718283,
                    Firstname="Sri",
                    Lastname="Chatala",
                    Phoneno="647-786-6026",
                    Course="Software Engineering Technology",
                    Email="schatala@my.centennialcollege.ca"
                },
                new Student{
                    StudentID=300718284,
                    Firstname="Alagu",
                    Lastname="Murugappan",
                    Phoneno="647-786-6325",
                    Course="Software Engineering Technology",
                    Email="malagu@my.centennialcollege.ca"
                },
                  new Student{
                    StudentID=300718285,
                    Firstname="Vangli",
                    Lastname="Rajan",
                    Phoneno="647-786-6363",
                    Course="Software Engineering Technology",
                    Email="rvangli@my.centennialcollege.ca"
                }
            };
            foreach (var item in student)
            {
                context.Students.Add(item);
            }
            context.SaveChanges();

            var campus = new List<Campus>
            {
                new Campus
                {
                    CampusName="Progress Campus",
                    CampusAddress="941 Progress Avenue",
                    City="Toronto",
                    Province="Ontario",
                    PostalCode="M1B3X7",
                    Phone="647-405-7686"
                },
                new Campus
                {
                    CampusName=" Morningside Campus",
                    CampusAddress="940 Progress Avenue",
                    City="Toronto",
                    Province="Ontario",
                    PostalCode="M1T7R3",
                    Phone="416-567-4587"
                }
            };
            foreach (var item in campus)
            {
                context.Campuses.Add(item);
            }
            context.SaveChanges();


            var Departments = new List<Department>
            {
                new Department
                {
                    dept_name ="ICET",
                    room_no = "A12",
                    campus_Id =1
                 },
                new Department
                {
                    dept_name ="ENGLISH",
                    room_no = "E12",
                    campus_Id =1
                }
            };
            foreach (var dept in Departments)
            {
                context.Departments.Add(dept);
            }
            context.SaveChanges();



            var Advisors = new List<Advisor>
            {
                new Advisor
                {
                    Firstname="Sara",
                    Lastname="sashthri",
                    Email="Sara@centennial.com",
                    Phoneno="416-567-4587",
                    dept_Id=1
                },
                new Advisor
                {
                    Firstname="Advisor1",
                    Lastname="Advisor1",
                    Email="Advisor1@centennial.com",
                    Phoneno="416-567-4517",
                    dept_Id=2
                }
            };
            foreach (var advisor in Advisors)
            {
                context.Advisors.Add(advisor);
            }
            context.SaveChanges();



            var Tokens = new List<Token>
            {
                new Token
                {
                    tokenid="ICET-01",
                    student_id=1,
                    dept_Id=1,
                    createdTime =new DateTime(2015,11,12,12,30,00),
                    issue="unable to register a subject",
                    status= "Active"
                },
                new Token
                {
                    tokenid="ICET-02",
                    student_id=2,
                    dept_Id=1,
                    createdTime =new DateTime(2015,11,12,12,30,00),
                    issue="unable to pay fees online",
                    status= "Active"
                }
            };
            foreach (var token in Tokens)
            {
                context.Tokens.Add(token);
            }
            context.SaveChanges();
        }
    }
}
