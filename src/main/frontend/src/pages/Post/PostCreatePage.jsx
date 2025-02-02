import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axiosInstance';
import '../../style/PostStyles.css';

const PostCreatePage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    title: '',
    content: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/posts/create', formData);
      alert('작성하기 성공');
      navigate('/');
    } catch (error) {
      alert('게시글 작성 실패');
    }
  };

  return (
    <div className="container">
      <h2 className="post-create-title">게시물 작성하기</h2>
      <form className="post-create-form" onSubmit={handleSubmit}>
        <input
          className="post-input"
          type="text"
          name="title"
          placeholder="제목"
          onChange={handleChange}
        />
        <textarea
          className="post-textarea"
          name="content"
          placeholder="내용을 입력하세요"
          onChange={handleChange}
        />
        <button className="post-submit-btn" type="submit">
          작성하기
        </button>
      </form>
    </div>
  );
};

export default PostCreatePage;
