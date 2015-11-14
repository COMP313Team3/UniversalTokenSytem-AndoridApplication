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

        [HttpPost]
        public HttpResponseMessage createToken(Token Newtoken)
        {

            var count = dbcontext.Tokens.Where(tok => string.Compare(tok.tokenid,Newtoken.tokenid) ==0 ).Count();

            if (count !=0)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound));
            }

            Newtoken.status = "Active";
            dbcontext.Tokens.Add(Newtoken);
            dbcontext.SaveChanges();

            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, Newtoken);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }

        [HttpPost]
        public HttpResponseMessage CloseToken(int tokenId,Token NewToken)
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
                catch
                {
                    return Request.CreateErrorResponse(HttpStatusCode.NotModified, "Failed to update");
                }
               
            }
            else
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, "No Tokens found for the id passed");
            }
          
        }

        [HttpGet]
        public HttpResponseMessage RetrieveTokensByDept([FromUri]int deptID)
        {

            List<Token> tokens = dbcontext.Tokens.Where(tok => tok.dept_Id == deptID).ToList<Token>();

          

            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, tokens);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }

        [HttpGet]
        public HttpResponseMessage RetrieveTokensForStudent([FromUri]int studentID)
        {
            var tokens = dbcontext.Tokens.Where(tok => tok.student_id  == studentID);
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, tokens);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }

        [HttpGet]
        public HttpResponseMessage RetrieveTokensCountByDept([FromUri]int departmentID)
        {
            var tokenscount = dbcontext.Tokens.Where(tok => tok.dept_Id == departmentID).Count();
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, tokenscount);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }


    }
}
