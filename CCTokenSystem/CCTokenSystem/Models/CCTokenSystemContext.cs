using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Linq;
using System.Web;
using CCTokenSystem.Models;

namespace CCTokenSystem.Models
{
    public class CCTokenSystemContext : DbContext
    {
        // You can add custom code to this file. Changes will not be overwritten.
        // 
        // If you want Entity Framework to drop and regenerate your database
        // automatically whenever you change your model schema, please use data migrations.
        // For more information refer to the documentation:
        // http://msdn.microsoft.com/en-us/data/jj591621.aspx
    
        public CCTokenSystemContext() : base("name=CCTokenSystemContext")
        {
        }


        //Initialization of table to into database
        public DbSet<Student> Students { get; set; }

        //Initialization of Campus table
        public DbSet<Campus> Campuses { get; set; }

        //Initialization of Departments table
        public DbSet<Department> Departments { get; set; }

        //onModelCreating method will remove pluralizing the model name
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Conventions.Remove<PluralizingTableNameConvention>();
        }
    
    }
}
