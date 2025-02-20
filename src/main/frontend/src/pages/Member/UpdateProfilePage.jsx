import React, { useState, useEffect } from 'react';
import {
  uploadProfileImage,
  deleteProfileImage,
  getProfileImage,
} from '../../api/ProfileApi';
import api from '../../api/axiosInstance';
import { useNavigate } from 'react-router-dom';

const UpdateProfilePage = () => {
  const navigate = useNavigate();
  const [profileImage, setProfileImage] = useState('/default-profile.png'); // ✅ 기본 이미지 설정
  const [selectedFile, setSelectedFile] = useState(null);
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  // ✅ 최신 프로필 이미지 불러오기
  const fetchProfileImage = async () => {
    try {
      const response = await api.get('/members/me'); // ✅ 회원 정보 다시 불러오기
      const imageUrl = getProfileImage(response.data.data.profileImageURL);
      setProfileImage(imageUrl); // ✅ 최신 프로필 이미지 상태 반영
    } catch (error) {
      console.warn('⚠ 프로필 이미지 가져오기 실패:', error);
    }
  };

  // ✅ 페이지 로드 시 프로필 이미지 불러오기
  useEffect(() => {
    fetchProfileImage();
  }, []);

  // ✅ 회원정보 수정 (이메일 & 비밀번호 변경)
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.put('/members/update', formData);
      alert('✅ 회원정보 수정 완료!');
      navigate('/profile');
    } catch (error) {
      alert(error.response?.data?.message || '❌ 회원정보 수정 실패');
    }
  };

  // ✅ 입력값 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // ✅ 파일 선택 시 미리보기 업데이트
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setSelectedFile(file);
    if (file) {
      const fileURL = URL.createObjectURL(file);
      setProfileImage(fileURL); // ✅ 선택한 이미지 미리보기로 표시
    }
  };

  // ✅ 프로필 이미지 업로드
  const handleUpload = async () => {
    if (!selectedFile) return alert('⚠ 파일을 선택하세요!');
    try {
      const imageUrl = await uploadProfileImage(selectedFile);
      setProfileImage(imageUrl); // ✅ 업로드 후 UI에 즉시 반영
      await fetchProfileImage(); // ✅ 최신 프로필 이미지 다시 불러오기
      alert('✅ 프로필 이미지 업로드 완료!');
    } catch (error) {
      console.error('❌ 업로드 실패:', error);
      alert('❌ 업로드 실패! 다시 시도해주세요.');
    }
  };

  // ✅ 프로필 이미지 삭제
  const handleDelete = async () => {
    try {
      await deleteProfileImage();
      setProfileImage(getProfileImage(null)); // ✅ 기본 이미지로 변경
      alert('✅ 프로필 이미지 삭제 완료!');
    } catch (error) {
      console.error('❌ 삭제 실패:', error);
      alert('❌ 삭제 실패! 다시 시도해주세요.');
    }
  };

  return (
    <div className="container">
      <h2 className="auth-title">회원정보 수정</h2>

      {/* ✅ 프로필 이미지 미리보기 */}
      <div className="profile-image-container">
        <img src={profileImage} alt="프로필" className="profile-image" />
      </div>

      {/* ✅ 파일 선택 & 업로드 버튼 */}
      <div className="profile-image-actions">
        <input type="file" accept="image/*" onChange={handleFileChange} />
        <button className="upload-btn" onClick={handleUpload}>
          업로드
        </button>
        <button className="delete-btn" onClick={handleDelete}>
          삭제
        </button>
      </div>

      {/* ✅ 이메일 & 비밀번호 변경 */}
      <form className="auth-card" onSubmit={handleSubmit}>
        <input
          className="auth-input"
          type="email"
          name="email"
          placeholder="새로운 이메일"
          onChange={handleChange}
          required
        />
        <input
          className="auth-input"
          type="password"
          name="password"
          placeholder="새로운 비밀번호"
          onChange={handleChange}
          required
        />
        <button className="profile-update-btn" type="submit">
          수정하기
        </button>
      </form>
    </div>
  );
};

export default UpdateProfilePage;
