using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace CCTokenSystem.Models
{
    public class Department
    {
        [Key]
        public int dept_Id { get; set; }
        public string dept_name { get; set; }
        public int campus_Id { get; set; }

        public virtual Campus Campus { get; set; }
        public virtual ICollection<Advisor> Advisors{ get; set;}

    }
}