# UTS-ADSE-5P

## Overview

Repository ini berisi aplikasi web berbasis Spring Boot, JSP, dan Camunda Platform untuk memproses pengajuan izin mahasiswa. Pengguna mengisi nama mahasiswa, alasan izin, dan jumlah hari melalui halaman web. Data tersebut diteruskan ke process engine Camunda untuk dieksekusi berdasarkan model BPMN yang dideploy otomatis saat aplikasi dijalankan.

Selain workflow pengajuan izin, aplikasi tetap menyediakan alur Hello World yang dapat dijalankan melalui endpoint `/start-process`.

## Objective

Implementasi ini bertujuan untuk:

1. menyediakan formulir pengajuan izin yang mudah digunakan;
2. memodelkan validasi dan keputusan persetujuan secara eksplisit dalam BPMN;
3. menerapkan aturan bisnis berbasis jumlah hari izin secara konsisten pada delegate dan gateway;
4. menyediakan observabilitas eksekusi proses melalui Camunda Cockpit; dan
5. mempertahankan perilaku workflow Hello World yang sudah tersedia.

## BPMN Flow

Model proses tersimpan pada `src/main/resources/processes/pengajuan-izin.bpmn` dengan process id `pengajuanIzin`. Alurnya adalah:

```text
Start
  -> Validasi pengajuan (service task)
  -> Exclusive gateway: Status pengajuan
       -> APPROVED -> Izin disetujui -> End
       -> REJECTED -> Izin ditolak -> End
```

Service task validasi memanggil `com.utsrpl.projekuts.workflow.LeaveRequestDelegate`. Delegate membaca variabel proses `hari`, menghasilkan variabel `status`, kemudian exclusive gateway memilih sequence flow berdasarkan nilai tersebut. File BPMN juga memiliki BPMN Diagram Interchange (DI), sehingga diagram dapat dibuka dan dirender oleh Camunda Modeler.

## Business Rule

Aturan keputusan yang digunakan:

| Jumlah hari | Status |
|---:|---|
| `hari <= 3` | `APPROVED` |
| `hari > 3` | `REJECTED` |

Nilai jumlah hari harus berupa bilangan positif. Nama mahasiswa dan alasan juga wajib diisi. Nilai input dikirim sebagai process variables `nama`, `alasan`, dan `hari`.

## Implementation Components and URLs

Komponen utama:

- `PageController`: menyediakan GET dan POST untuk halaman pengajuan izin.
- `LeaveRequestDelegate`: melakukan validasi jumlah hari dan menetapkan status proses.
- `pengajuan-izin.bpmn`: mendefinisikan service task, exclusive gateway, kondisi sequence flow, end event, dan diagram layout.
- `pengajuan-izin.jsp`: formulir JSP dan tampilan hasil status.
- `camunda-bpm-spring-boot-starter`: process engine Camunda.
- `camunda-bpm-spring-boot-starter-webapp`: Camunda Webapp, termasuk Cockpit.
- H2 in-memory database: penyimpanan runtime Camunda selama aplikasi berjalan.

Dengan server berjalan pada port default `8080`, URL yang tersedia:

| URL | Fungsi |
|---|---|
| `http://localhost:8080/pengajuan-izin` | Form GET dan target POST pengajuan izin |
| `http://localhost:8080/camunda/app/cockpit/` | Camunda Cockpit |
| `http://localhost:8080/start-process` | Menjalankan workflow Hello World |
| `http://localhost:8080/hello-jsp` | Menampilkan halaman JSP Hello World |

Camunda membuat user administrator dari konfigurasi aplikasi. Pada konfigurasi lokal ini, id administrator adalah `admin`; password mengikuti nilai yang tersedia pada `application.properties`.

## How to Run and Test

Gunakan PowerShell dari root repository:

```powershell
Set-Location "<project-folder>"
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

Kemudian buka halaman `pengajuan-izin`, isi ketiga field, dan kirim formulir. Pengujian perilaku minimum:

1. input `hari = 3` menghasilkan `APPROVED`;
2. input `hari = 4` menghasilkan `REJECTED`;
3. endpoint `/start-process` tetap menghasilkan respons sukses untuk workflow Hello World; dan
4. halaman Cockpit dapat dibuka serta menampilkan deployment dan riwayat proses Camunda.

Pengujian otomatis dapat dijalankan dengan:

```powershell
.\mvnw.cmd test
```

Maven test memverifikasi bahwa Spring context, Camunda process engine, dan deployment BPMN dapat diinisialisasi. Verifikasi endpoint dilakukan saat aplikasi berjalan karena memerlukan web server aktif dan database runtime.

## Camunda Cockpit Evidence

Cockpit digunakan sebagai bukti observasional bahwa process definition `pengajuanIzin` telah dideploy ke Camunda dan instance proses dapat dieksekusi. Setelah aplikasi berjalan, buka:

```text
http://localhost:8080/camunda/app/cockpit/
```

Pada Cockpit, process definition pengajuan izin dapat ditinjau melalui daftar proses dan instance eksekusinya. Instance dengan `hari = 3` menunjukkan jalur persetujuan, sedangkan instance dengan `hari = 4` menunjukkan jalur penolakan.

Empat screenshot evidence yang diharapkan untuk dokumentasi ini adalah:

1. diagram BPMN pengajuan izin;
2. hasil formulir dengan status `APPROVED`;
3. hasil formulir dengan status `REJECTED`; dan
4. daftar process definition pada Camunda Cockpit.

File screenshot tersebut tidak tersedia sebagai file di checkout saat README ini diperbarui, sehingga tautan Markdown belum ditambahkan agar tidak menghasilkan path palsu. Setelah gambar ditempatkan di repository, gunakan direktori relatif, misalnya `docs/images/`, dan tambahkan referensi hanya untuk file yang benar-benar ada:

```text
docs/images/pengajuan-izin-bpmn.png
docs/images/pengajuan-izin-approved.png
docs/images/pengajuan-izin-rejected.png
docs/images/camunda-cockpit-process-list.png
```

Pengguna perlu menambahkan empat file tersebut ke lokasi relatif yang disepakati sebelum README dapat memuat tautan screenshot yang valid.

## Conclusion

Workflow pengajuan izin telah menghubungkan formulir JSP, controller Spring MVC, delegate Java, model BPMN, dan Camunda Cockpit dalam satu alur eksekusi. Keputusan proses bersifat deterministik: izin sampai tiga hari disetujui, sedangkan izin lebih dari tiga hari ditolak. Struktur ini mempertahankan workflow Hello World sekaligus menyediakan proses izin yang dapat dideploy, diuji, dan diamati melalui Camunda.
