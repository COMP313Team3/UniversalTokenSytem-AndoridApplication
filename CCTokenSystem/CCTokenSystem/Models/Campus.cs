using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace CCTokenSystem.Models
{
    public class Campus
    {
        [Key]
        public int campus_Id { get; set; }
        public string campus_name { get; set; }
        public string address1 { get; set; }
        public string city { get; set; }
        public string province { get; set; }
        public string postal_code { get; set; }
        public int phone { get; set; }

        public virtual List<Department> campus_departments { get; set; }
    }
}