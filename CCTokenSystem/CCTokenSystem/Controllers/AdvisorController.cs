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
    public class AdvisorController : ApiController
    {

        //
        CCTokenSystemContext dbcontext = new CCTokenSystemContext();

        [HttpGet]
        public IEnumerable<Advisor> GetAllAdvisors()
        {
            return dbcontext.Advisors.AsEnumerable<Advisor>();
        }

        [HttpGet]
        public HttpResponseMessage GetAdvisorbyID(int Id)
        {
            var advisor = dbcontext.Advisors.Where(adv => adv.Advisor_Id == Id).FirstOrDefault();

            if (advisor == null)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound));
            }

            Advisor advi = (Advisor)advisor;


            advi.campusid = advi.Department.campus_Id;

            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, advisor);

            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            return response;
        }


        [HttpPut]
        public HttpResponseMessage UpdateAdvisor(Advisor advisor)
        {
            if (advisor != null)
            {
                dbcontext.Entry(advisor).State = EntityState.Modified;
            }

            try
            {
                dbcontext.SaveChanges();
            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
            }


            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, advisor);

            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            return response;
        }

        [HttpPost]
        public HttpResponseMessage CreateAdvisor(Advisor advisor)
        {
            var checkEmail = dbcontext.Advisors.Where(a_name => a_name.Email == advisor.Email).Any();
            if (!checkEmail)
            {
                dbcontext.Advisors.Add(advisor);
                try
                {
                    dbcontext.SaveChanges();
                }
                catch (Exception ex)
                {
                    return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
                }

                HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, advisor);

                response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

                return response;
            }
            HttpResponseMessage res = Request.CreateResponse(HttpStatusCode.NotFound, "Not Found");
            return res;
        }
        [HttpDelete]
        public HttpResponseMessage DeleteAdvisor(int AdvisorID)
        {
            Advisor advisor = dbcontext.Advisors.Find(AdvisorID);

            if (advisor == null)
            {
                return Request.CreateResponse(HttpStatusCode.NotFound);
            }
            dbcontext.Advisors.Remove(advisor);

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
        [HttpGet]
        public HttpResponseMessage GetAdvisor([FromUri] string email, string password)
        {
            if (email == null && password==null)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }
            List<Advisor> advisorInfo = dbcontext.Advisors.Where(a => a.Email == email && a.Password == password).ToList();
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, advisorInfo);

            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            return response;
        }
    }
}
