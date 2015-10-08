using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using CCTokenSystem.Models;
using System.Net.Http.Headers;

namespace CCTokenSystem.Controllers
{
    public class CampusesController : ApiController
    {
        private CCTokenSystemContext dbcontext = new CCTokenSystemContext();

        //Get all the campuses details
        [HttpGet]
        public IEnumerable<Campus> GetAllCampses()
        {
            return dbcontext.Campuses.AsEnumerable<Campus>();
        }

        //Get campus information by using ID
        [HttpGet]
        public Campus GetCampusByID(int Id)
        {
            Campus campus = dbcontext.Campuses.Find(Id);
            if (campus == null)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound));
            }
            return campus;
        }

        //Update Campus detials by using CampusId
        [HttpPut]
        public HttpResponseMessage UpdateCampus(Campus campus)
        {
            if (campus != null)
            {
                dbcontext.Entry(campus).State = EntityState.Modified;
            }

            try
            {
                dbcontext.SaveChanges();
            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
            }


            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, campus);

            response.StatusCode = HttpStatusCode.Created;

            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            return response;
        }

        //Add campus details
        [HttpPost]
        public HttpResponseMessage CreateCampus(Campus campus)
        {
            dbcontext.Campuses.Add(campus);
            try
            {
                dbcontext.SaveChanges();
            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
            }

            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.Created, campus);

            response.StatusCode = HttpStatusCode.Created;

            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            return response;
        }

        //Performing delete campus details by using CampusID
        [HttpDelete]
        public HttpResponseMessage DeleteCampus(int Id)
        {
            Campus campus = dbcontext.Campuses.Find(Id);

            if (campus == null)
            {
                return Request.CreateResponse(HttpStatusCode.NotFound);
            }
            dbcontext.Campuses.Remove(campus);

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
