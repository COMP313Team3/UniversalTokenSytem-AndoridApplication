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
        public int CamspusId { get; set; }
        public string CampusName { get; set; }
        public string CampusAddress { get; set; }
        public string City { get; set; }
        public string Province { get; set; }
        public string PostalCode { get; set; }
        public string Phone { get; set; }

        public virtual List<Department> campus_departments { get; set; }
    }
}