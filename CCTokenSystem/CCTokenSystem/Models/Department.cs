using Newtonsoft.Json;
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
        public string room_no { get; set; }

        [ForeignKey("Campus")]
        public int campus_Id { get; set; }
        [JsonIgnore]
        public virtual Campus Campus { get; set; }

    }
}