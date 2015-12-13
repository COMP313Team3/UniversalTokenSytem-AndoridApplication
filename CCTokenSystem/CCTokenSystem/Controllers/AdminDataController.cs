using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using OfficeOpenXml;
using CCTokenSystem.Models;

namespace CCTokenSystem.Controllers
{
    public class Datasheet
    {
        public int id { get; set; }
        public string Name { get; set; }
        public Boolean TobeUploaded { get; set; }
    }
    public class AdminDataController : Controller
    {
        CCTokenSystemContext dbcontext = new CCTokenSystemContext();

        // GET: AdminData
        public ActionResult Index()
        {
            return View();
        }

        public void upload()
        {


        }

        [HttpPost]
        public ActionResult Upload(List<Datasheet> lstDatasheets)
        {

            if (Request != null)
            {
                if (Request.Form["LoadDatasheets"] != null)
                {
                    CCTokenSystemContext dbcontext = new CCTokenSystemContext();
                    HttpPostedFileBase file = Request.Files["DataFileupload"];
                    string fileExtension = System.IO.Path.GetExtension(Request.Files["DataFileupload"].FileName);
                    Session["resourceFile"] = file;
                    if (fileExtension == ".xls" || fileExtension == ".xlsx")
                    {
                        var package = new ExcelPackage(file.InputStream);
                        lstDatasheets = new List<Datasheet>();
                        int id = 0;
                        foreach (ExcelWorksheet ws in package.Workbook.Worksheets)
                        {
                            Datasheet ds = new Datasheet();
                            ds.id = ++id;
                            ds.Name = ws.Name;
                            lstDatasheets.Add(ds);
                        }
                    }
                    else {
                        ViewData["error"] = "Please, Uplaod Excel file only";
                    }
                    return View("Index", lstDatasheets);
                }
                if (Request.Form["LoadData"] != null)
                {
                    if (lstDatasheets != null && lstDatasheets.Count >= 1)
                    {
                        HttpPostedFileBase file = (HttpPostedFileBase)Session["resourceFile"];

                        foreach (Datasheet ds in lstDatasheets)
                        {

                            if (ds.Name.Equals("students") && ds.TobeUploaded)
                            {
                                LoadStudent(file);
                            }
                            if (ds.Name.Equals("Depts") && ds.TobeUploaded)
                            {
                                LoadDept(file);
                            }
                            if (ds.Name.Equals("campus") && ds.TobeUploaded)
                            {
                                LoadCampus(file);
                            }
                            if (ds.Name.Equals("Advisors") && ds.TobeUploaded)
                            {
                                LoadAdvisors(file);
                            }

                        }
                    }
                }
            }
            ViewData["message"] = "Data successfully uploaded";
            return View("Index");
        }
     
        private void LoadStudent(HttpPostedFileBase file)
        {
            using (var package = new ExcelPackage(file.InputStream))
            {
                var studentworkSheet = package.Workbook.Worksheets["students"];
                var studentnoOfRow = studentworkSheet.Dimension.End.Row;

                for (int rowIterator = 2; rowIterator <= studentnoOfRow; rowIterator++)
                {
                    Student student = new Student();
                    student.StudentID = int.Parse(studentworkSheet.Cells[rowIterator, 1].Value.ToString());
                    student.Firstname = studentworkSheet.Cells[rowIterator, 2].Value.ToString();
                    student.Lastname = studentworkSheet.Cells[rowIterator, 3].Value.ToString();
                    student.Phoneno = studentworkSheet.Cells[rowIterator, 4].Value.ToString();
                    student.Course = studentworkSheet.Cells[rowIterator, 5].Value.ToString();
                    student.Email = studentworkSheet.Cells[rowIterator, 6].Value.ToString();
                    dbcontext.Students.Add(student);
                    dbcontext.SaveChanges();
                }
            }
        }
        private void LoadDept(HttpPostedFileBase file)
        {
            using (var package = new ExcelPackage(file.InputStream))
            {
                var deptworkSheet = package.Workbook.Worksheets["Depts"];
                var deptnoOfRow = deptworkSheet.Dimension.End.Row;

                for (int rowIterator = 2; rowIterator <= deptnoOfRow; rowIterator++)
                {
                    Department dept = new Department();
                    dept.dept_Id = int.Parse(deptworkSheet.Cells[rowIterator, 1].Value.ToString());
                    dept.dept_name = deptworkSheet.Cells[rowIterator, 2].Value.ToString();
                    dept.room_no = deptworkSheet.Cells[rowIterator, 3].Value.ToString();
                    dept.campus_Id = int.Parse(deptworkSheet.Cells[rowIterator, 4].Value.ToString());
                    dbcontext.Departments.Add(dept);
                    dbcontext.SaveChanges();
                }
            }
        }
        private void LoadCampus(HttpPostedFileBase file)
        {
            using (var package = new ExcelPackage(file.InputStream))
            {
                var campusWorkSheet = package.Workbook.Worksheets["campus"];
                var campusWorkSheetnoOfRow = campusWorkSheet.Dimension.End.Row;

                for (int rowIterator = 2; rowIterator <= campusWorkSheetnoOfRow; rowIterator++)
                {
                    Campus campus = new Campus();
                    campus.CampusId = int.Parse(campusWorkSheet.Cells[rowIterator, 1].Value.ToString());
                    campus.CampusName = campusWorkSheet.Cells[rowIterator, 2].Value.ToString();
                    campus.CampusAddress = campusWorkSheet.Cells[rowIterator, 3].Value.ToString();
                    campus.City = campusWorkSheet.Cells[rowIterator, 4].Value.ToString();
                    campus.Province = campusWorkSheet.Cells[rowIterator, 5].Value.ToString();
                    campus.PostalCode = campusWorkSheet.Cells[rowIterator, 6].Value.ToString();
                    campus.Phone = campusWorkSheet.Cells[rowIterator, 7].Value.ToString();
                    dbcontext.Campuses.Add(campus);
                    dbcontext.SaveChanges();
                }
            }
        }
        private void LoadAdvisors(HttpPostedFileBase file)
        {
            using (var package = new ExcelPackage(file.InputStream))
            {
                var advisorworkSheet = package.Workbook.Worksheets["Advisors"];
                var advisornoOfRow = advisorworkSheet.Dimension.End.Row;

                for (int rowIterator = 2; rowIterator <= advisornoOfRow; rowIterator++)
                {
                    Advisor advisor = new Advisor();
                    advisor.Firstname = advisorworkSheet.Cells[rowIterator, 1].Value.ToString();
                    advisor.Lastname = advisorworkSheet.Cells[rowIterator, 2].Value.ToString();
                    advisor.Email = advisorworkSheet.Cells[rowIterator, 3].Value.ToString();
                    advisor.Phoneno = advisorworkSheet.Cells[rowIterator, 4].Value.ToString();
                    advisor.dept_Id = int.Parse(advisorworkSheet.Cells[rowIterator, 5].Value.ToString());
                    dbcontext.Advisors.Add(advisor);
                    dbcontext.SaveChanges();
                }

            }
        }

    }


     
}