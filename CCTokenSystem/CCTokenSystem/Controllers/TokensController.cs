using CCTokenSystem.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Core.Objects;
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
        int stdTokenClosingTime = 10; // mins, this value is approximate , Time taken by advisor to close each ticket

        CCTokenSystemContext dbcontext = new CCTokenSystemContext();

        [HttpPost]
        public HttpResponseMessage createToken(Token Newtoken)
        {

            var count = dbcontext.Tokens.Where(tok => string.Compare(tok.tokenid, Newtoken.tokenid) == 0).Count();

            if (count != 0)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound));
            }

            Newtoken.createdTime = DateTime.Now;
            Newtoken.tokenid = GenearateTokenID(Newtoken.dept_Id);
            Newtoken.status = "Active";
            dbcontext.Tokens.Add(Newtoken);
            dbcontext.SaveChanges();

            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, Newtoken);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }

        [HttpPut]
        public HttpResponseMessage CloseToken(Token NewToken)
        {
            NewToken.department = null;
            NewToken.student = null;
            if (NewToken != null)
            {
                NewToken.closingTime = DateTime.Now;
                NewToken.status = "InActive";
                dbcontext.Entry(NewToken).State = EntityState.Modified;
            }

            try
            {
                dbcontext.SaveChanges();
            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
            }
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, NewToken);

            response.StatusCode = HttpStatusCode.Created;

            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            return response;
            //NewToken.department = null;
            //NewToken.student = null;
            //var tokentoupdate = dbcontext.Tokens.Where(tok => tok.tokenid == NewToken.tokenid);

            //Token token;

            //if (tokentoupdate != null)
            //{
            //    token = (Token)tokentoupdate;

            //    NewToken.closingTime = DateTime.Now;
            //    token.status = "InActive";
            //    token.Advisor_Id = 1;

            //    dbcontext.Entry(tokentoupdate).State = EntityState.Modified;

            //    try
            //    {
            //        dbcontext.SaveChanges();
            //        HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, token);
            //        response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            //        return response;
            //    }
            //    catch
            //    {
            //        return Request.CreateErrorResponse(HttpStatusCode.NotModified, "Failed to update");
            //    }

            //}
            //else
            //{
            //    return Request.CreateErrorResponse(HttpStatusCode.NotFound, "No Tokens found for the id passed");
            //}

        }



        [HttpGet]
        public HttpResponseMessage RetrieveTokensByDept([FromUri]int dID)
        {
            List<Token> tokens = dbcontext.Tokens.Where(tok => tok.dept_Id == dID &&  tok.status=="Active").ToList<Token>();
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, tokens);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }

        [HttpGet]
        public HttpResponseMessage RetrieveTokensForStudent([FromUri]int studentID)
        {
            List<Token> tokens = dbcontext.Tokens.Where(tok => tok.student_id == studentID).ToList<Token>();
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, tokens);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }

        [HttpGet]
        public HttpResponseMessage RetrieveTokensCountByDept([FromUri]int dept_Id)
        {
            var tokenscount = dbcontext.Tokens.Where(tok => tok.dept_Id == dept_Id).Count();
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, tokenscount);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }

        [HttpGet]
        public HttpResponseMessage RetrieveTokenById([FromUri]string token_Id)
        {
            Token token = getTokenbyId(token_Id);

            if (token != null)
            {
                token.approximateWaitTimeinMins = getTokenWaitTime(token_Id);
            }
            HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, token);
            response.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            return response;
        }

        [HttpGet]
        public string GenearateTokenID(int departmentID)
        {
            string tokenid = "";
            DateTime dt = DateTime.Now.Date;
            var lastTokenGenerated = dbcontext.Tokens.Where(tok => tok.dept_Id == departmentID && DbFunctions.TruncateTime(tok.createdTime).Value == dt).OrderBy(tok => tok.createdTime);
            Department department = dbcontext.Departments.Where(dept => dept.dept_Id == departmentID).FirstOrDefault<Department>();

            if (lastTokenGenerated.Count() != 0)
            {
                tokenid = lastTokenGenerated.FirstOrDefault<Token>().tokenid;
                tokenid = department.dept_name + "-" + (int.Parse(tokenid.Substring(tokenid.IndexOf("-") + 1)) + 1).ToString().PadLeft(2, '0');
            }
            else
            {
                tokenid = department.dept_name + "-01";
            }

            //this will generate new id if the id already exists
            while (getTokenbyId(tokenid) != null)
            {
                tokenid = department.dept_name + "-" + (int.Parse(tokenid.Substring(tokenid.IndexOf("-") + 1)) + 1).ToString().PadLeft(2, '0');
            }
            return tokenid;
        }

        private Token getTokenbyId(string tokenId)
        {
            Token token = dbcontext.Tokens.Where(tok => tok.tokenid == tokenId).FirstOrDefault<Token>();
            return token;
        }

        private int getTokenWaitTime(string tokenId)
        {

            Token studentToken = getTokenbyId(tokenId);

            List<Token> waitingTokens = dbcontext.Tokens.Where(tok => tok.status == "Active" && tok.dept_Id == studentToken.dept_Id && tok.createdTime < studentToken.createdTime).ToList<Token>();

            return stdTokenClosingTime * waitingTokens.Count;

        }
    }
}
