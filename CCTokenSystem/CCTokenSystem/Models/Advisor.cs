using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CCTokenSystem.Models
{
    public class Advisor
    {
        [Key]
        public int Advisor_Id { get; set; }
        public string Firstname { get; set; }
        public string Lastname { get; set; }
        public string Email { get; set; }
        public string Phoneno { get; set; }
        [ForeignKey("Department")]
        public int dept_Id { get; set; }
        [JsonIgnore]
        public virtual Department Department { get; set; }
    }
}