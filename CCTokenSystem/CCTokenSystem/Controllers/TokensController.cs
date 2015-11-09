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
    public class TokensController : ApiController
    {
        CCTokenSystemContext dbcontext = new CCTokenSystemContext();


        public HttpResponseMessage createToken(Token Newtoken)
        {

            var token = dbcontext.Tokens.Where(tok => tok.tokenid == Newtoken.tokenid);

            if (token == null)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound));
            }

            Newtoken.status = "Active";
            dbcontext.Tokens.Add(Newtoken);

            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, Newtoken);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }


        public HttpResponseMessage CloseToken(Token NewToken)
        {
            var tokentoupdate = dbcontext.Tokens.Where(tok => tok.tokenid == NewToken.tokenid);

            Token token;

            if (tokentoupdate != null)
            {
                 token = (Token)tokentoupdate;

                token.status = "InActive";
                token.Advisor_Id = NewToken.Advisor_Id;

                dbcontext.Entry(tokentoupdate).State = EntityState.Modified;

                try
                {
                    dbcontext.SaveChanges();
                    HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, token);
                    response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
                    return response;
                }
                catch (Exception ex)
                {
                    return Request.CreateErrorResponse(HttpStatusCode.NotModified, "Failed to update");
                }
               
            }
            else
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, "No Tokens found for the id passed");
            }
          
        }


        public HttpResponseMessage RetrieveTokensByDept([FromUri]int deptID)
        {

            var tokens = dbcontext.Tokens.Where(tok => tok.dept_Id == deptID);
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, tokens);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }
        public HttpResponseMessage RetrieveTokensForStudent([FromUri]int studentID)
        {
            var tokens = dbcontext.Tokens.Where(tok => tok.student_id  == studentID);
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, tokens);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }

        public HttpResponseMessage RetrieveTokensCountByDept([FromUri]int deptID)
        {
            var tokenscount = dbcontext.Tokens.Where(tok => tok.dept_Id == deptID).Count();
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, tokenscount);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }


    }
}
