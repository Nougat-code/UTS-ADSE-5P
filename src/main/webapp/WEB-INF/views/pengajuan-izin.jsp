<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <title>Pengajuan Cuti Mahasiswa</title>
</head>
<body>
    <h1>Pengajuan Cuti Mahasiswa</h1>

    <form method="post" action="${pageContext.request.contextPath}/pengajuan-izin">
        <p>
            <label for="nama">Nama mahasiswa</label>
            <input id="nama" name="nama" type="text" value="${nama}" required>
        </p>
        <p>
            <label for="alasan">Alasan</label>
            <textarea id="alasan" name="alasan" required>${alasan}</textarea>
        </p>
        <p>
            <label for="hari">Jumlah hari</label>
            <input id="hari" name="hari" type="number" min="1" value="${hari}" required>
        </p>
        <button type="submit">Ajukan cuti</button>
    </form>

    <c:if test="${not empty error}">
        <p role="alert">${error}</p>
    </c:if>

    <c:if test="${not empty status}">
        <h2>Hasil Pengajuan</h2>
        <p>Status: <strong>${status}</strong></p>
        <p>Process instance: ${processInstanceId}</p>
    </c:if>
</body>
</html>
