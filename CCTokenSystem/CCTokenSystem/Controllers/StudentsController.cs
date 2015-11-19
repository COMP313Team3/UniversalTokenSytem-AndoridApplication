using CCTokenSystem.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Web.Http;

namespace CCTokenSystem.Controllers
{
    public class StudentsController : ApiController
    {
        CCTokenSystemContext dbcontext = new CCTokenSystemContext();

        [HttpGet]
        public IEnumerable<Student> GetAllStudents()
        {
            return dbcontext.Students.AsEnumerable<Student>();
        }
        public HttpResponseMessage GetbyStudentID([FromUri]int StudentID)
        {
            var student = dbcontext.Students.Where(sid=>sid.StudentID==StudentID);
            if (student == null)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound));
            }
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, student);

            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            return response;
        }
        [HttpGet]
        public Student GetStudentByID(int Id)
        {
            Student student = dbcontext.Students.Find(Id);

            if (student == null)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound));
            }
            return student;
        }

        [HttpPut]
        public HttpResponseMessage UpdateStudent(int id,Student student)
        {
            if (student != null)
            {
                dbcontext.Entry(student).State = EntityState.Modified;
            }

            try
            {
                dbcontext.SaveChanges();
            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
            }


            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, student);

            response.StatusCode = HttpStatusCode.Created;

            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            return response;
        }

        [HttpPost]
        public HttpResponseMessage CreateStudent(Student student)
        {
            var checkid = dbcontext.Students.Where(st_id => st_id.StudentID == student.StudentID).Any();
            if (!checkid) {
                dbcontext.Students.Add(student);
                try
                {
                    dbcontext.SaveChanges();
                }
                catch (Exception ex)
                {
                    return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
                }

                HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.Created, student);

                response.StatusCode = HttpStatusCode.Created;

                response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

                return response;
            }
            
            HttpResponseMessage res = Request.CreateResponse(HttpStatusCode.OK, "Not Found");
            return res;

        }

        [HttpDelete]
        public HttpResponseMessage DeleteStudent(int Id)
        {
            Student student = dbcontext.Students.Find(Id);

            if (student == null)
            {
                return Request.CreateResponse(HttpStatusCode.NotFound);
            }
            dbcontext.Students.Remove(student);

            try
            {
                dbcontext.SaveChanges();
            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
            }

            return Request.CreateResponse(new HttpResponseMessage(HttpStatusCode.OK));
        }
    }
}
