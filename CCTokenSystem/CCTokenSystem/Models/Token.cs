using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace CCTokenSystem.Models
{
    public class Token
    {
        [Key]
        public int Id { get; set; }
        public string tokenid { get; set; }

        public int student_id { get; set; }

        public int dept_Id { get; set; }

        public int CampusId { get; set; }

        public DateTime createdTime { get; set; }
        public DateTime closingTime { get; set; }
        public string issue { get; set; }
        public char status { get; set; }
        public int Advisor_Id { get; set; }

        [ForeignKey("student_id")]
        public virtual Student student { get; set; }


        [ForeignKey("Advisor_Id")]
        public virtual Advisor advisor { get; set; }

    }
}
