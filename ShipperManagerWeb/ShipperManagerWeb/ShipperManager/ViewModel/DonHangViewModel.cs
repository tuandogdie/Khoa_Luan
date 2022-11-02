using ShipperManager.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Xml;

namespace ShipperManager.ViewModel
{
    public class DonHangViewModel
    {
        public string Id { get; set; }
        public string MaNhanVien { get; set; }
        public string MaKhachHang { get; set; }
        public string MaPhuongThucThanhToan { get; set; }
        public DateTime NgayTao { get; set; }

        public IEnumerable<SelectListItem> KhachHangListItem { get; set; }
        public IEnumerable<SelectListItem> PaymentMethodListItem { get; set; }
        public List<ChiTietDonHang> OrderDetailListItem { get; set; }

        public DonHangViewModel(List<ChiTietDonHang> orderDetailList)
        {
            Task.Run(() => InitKhachHangList()).Wait();
            Task.Run(() => InitPaymentMethodgList()).Wait();
            OrderDetailListItem = orderDetailList;
            NgayTao = DateTime.Now;
        }

        public DonHangViewModel()
        {

        }

        public async Task<DonHang> GetDonHang()
        {
            var created = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
            var kh = await DatabaseUtils.GetElementByKey<KhachHang>(TableCategory.KhachHang, MaKhachHang);
            var pttt = await DatabaseUtils.GetElementByKey<PhuongThucThanhToan>(TableCategory.PhuongThucThanhToan, MaPhuongThucThanhToan);
            var nv = await DatabaseUtils.GetElementByKey<NhanVien>(TableCategory.NhanVien, MaNhanVien);
            var dis = GetDrivingDistanceInMiles("12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, Thành phố Hồ Chí Minh", kh.Object.DiaChi);
            return new DonHang(nv.Object, kh.Object, created, pttt.Object, OrderDetailListItem,dis,25000);
        }

        /// <summary>
        /// Get Driving Distance In Miles based on Source and Destination.
        /// </summary>
        /// <param name="origin"></param>
        /// <param name="destination"></param>
        /// <returns></returns>
        public double GetDrivingDistanceInMiles(string origin, string destination)
        {
            string url = $"https://maps.googleapis.com/maps/api/distancematrix/xml?units=imperial&origins={origin}&destinations={destination}&key=AIzaSyAQ98LDwfFxMuX46r1us23C2x0g1b6oTy4";
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            WebResponse response = request.GetResponse();
            Stream dataStream = response.GetResponseStream();
            StreamReader sreader = new StreamReader(dataStream);
            string responsereader = sreader.ReadToEnd();
            response.Close();
            XmlDocument xmldoc = new XmlDocument();
            xmldoc.LoadXml(responsereader);
            if (xmldoc.GetElementsByTagName("status")[0].ChildNodes[0].InnerText == "OK")
            {
                XmlNodeList distance = xmldoc.GetElementsByTagName("distance");
                return Convert.ToDouble(distance[0].ChildNodes[1].InnerText.Replace(" mi", ""));
            }

            return 0;
        }

        private async Task InitKhachHangList()
        {
            var lst = new List<SelectListItem>();
            var customers = await DatabaseUtils.GetAllElement<KhachHang>(TableCategory.KhachHang);
            foreach (var item in customers)
            {
                var kh = item.Object;
                kh.Id = item.Key;
                var selectItem = new SelectListItem()
                {
                    Text = kh.Ten,
                    Value = kh.Id
                };
                lst.Add(selectItem);
            }
            KhachHangListItem = lst;
        }

        private async Task InitPaymentMethodgList()
        {
            var lst = new List<SelectListItem>();
            var elements = await DatabaseUtils.GetAllElement<PhuongThucThanhToan>(TableCategory.PhuongThucThanhToan);
            foreach (var item in elements)
            {
                var pttt = item.Object;
                pttt.Id = item.Key;
                var selectItem = new SelectListItem()
                {
                    Text = pttt.Ten,
                    Value = pttt.Id
                };
                lst.Add(selectItem);
            }
            PaymentMethodListItem = lst;
        }
    }
}