package com.leet.study.model;

import java.util.List;

public class ProblemData {
    /**
     * data : {"question":{"title":"Two Sum","content":"<p>Given an array of integers, return <strong>indices<\/strong> of the two numbers such that they add up to a specific target.<\/p>\r\n\r\n<p>You may assume that each input would have <strong><em>exactly<\/em><\/strong> one solution, and you may not use the <em>same<\/em> element twice.<\/p>\r\n\r\n<p><strong>Example:<\/strong><\/p>\r\n\r\n<pre>\r\nGiven nums = [2, 7, 11, 15], target = 9,\r\n\r\nBecause nums[<strong>0<\/strong>] + nums[<strong>1<\/strong>] = 2 + 7 = 9,\r\nreturn [<strong>0<\/strong>, <strong>1<\/strong>].\r\n<\/pre>\r\n\r\n<p>&nbsp;<\/p>\r\n","translatedTitle":"两数之和","translatedContent":"<p>给定一个整数数组 <code>nums<\/code>&nbsp;和一个目标值 <code>target<\/code>，请你在该数组中找出和为目标值的那&nbsp;<strong>两个<\/strong>&nbsp;整数，并返回他们的数组下标。<\/p>\n\n<p>你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。<\/p>\n\n<p><strong>示例:<\/strong><\/p>\n\n<pre>给定 nums = [2, 7, 11, 15], target = 9\n\n因为 nums[<strong>0<\/strong>] + nums[<strong>1<\/strong>] = 2 + 7 = 9\n所以返回 [<strong>0, 1<\/strong>]\n<\/pre>\n","topicTags":[{"name":"Array","slug":"array","translatedName":"数组"}]}}
     */

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        /**
         * question : {"title":"Two Sum","content":"<p>Given an array of integers, return <strong>indices<\/strong> of the two numbers such that they add up to a specific target.<\/p>\r\n\r\n<p>You may assume that each input would have <strong><em>exactly<\/em><\/strong> one solution, and you may not use the <em>same<\/em> element twice.<\/p>\r\n\r\n<p><strong>Example:<\/strong><\/p>\r\n\r\n<pre>\r\nGiven nums = [2, 7, 11, 15], target = 9,\r\n\r\nBecause nums[<strong>0<\/strong>] + nums[<strong>1<\/strong>] = 2 + 7 = 9,\r\nreturn [<strong>0<\/strong>, <strong>1<\/strong>].\r\n<\/pre>\r\n\r\n<p>&nbsp;<\/p>\r\n","translatedTitle":"两数之和","translatedContent":"<p>给定一个整数数组 <code>nums<\/code>&nbsp;和一个目标值 <code>target<\/code>，请你在该数组中找出和为目标值的那&nbsp;<strong>两个<\/strong>&nbsp;整数，并返回他们的数组下标。<\/p>\n\n<p>你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。<\/p>\n\n<p><strong>示例:<\/strong><\/p>\n\n<pre>给定 nums = [2, 7, 11, 15], target = 9\n\n因为 nums[<strong>0<\/strong>] + nums[<strong>1<\/strong>] = 2 + 7 = 9\n所以返回 [<strong>0, 1<\/strong>]\n<\/pre>\n","topicTags":[{"name":"Array","slug":"array","translatedName":"数组"}]}
         */

        private Question question;

        public Question getQuestion() {
            return question;
        }

        public void setQuestion(Question question) {
            this.question = question;
        }

        public static class Question {
            private String title;
            private String content;
            private String translatedTitle;
            private String translatedContent;
            private List<TopicTags> topicTags;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTranslatedTitle() {
                return translatedTitle;
            }

            public void setTranslatedTitle(String translatedTitle) {
                this.translatedTitle = translatedTitle;
            }

            public String getTranslatedContent() {
                return translatedContent;
            }

            public void setTranslatedContent(String translatedContent) {
                this.translatedContent = translatedContent;
            }

            public List<TopicTags> getTopicTags() {
                return topicTags;
            }

            public void setTopicTags(List<TopicTags> topicTags) {
                this.topicTags = topicTags;
            }

            public static class TopicTags {
                /**
                 * name : Array
                 * slug : array
                 */

                private String name;
                private String slug;
                private String translatedName;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSlug() {
                    return slug;
                }

                public void setSlug(String slug) {
                    this.slug = slug;
                }

                public String getTranslatedName() {
                    return translatedName;
                }

                public void setTranslatedName(String translatedName) {
                    this.translatedName = translatedName;
                }
            }
        }
    }
}
