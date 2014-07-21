<?php
/**
 * @version     1.0.0
 * @package     com_codeanalyzer
 * @copyright   © 2014. Все права защищены.
 * @license     GNU General Public License версии 2 или более поздней; Смотрите LICENSE.txt
 * @author      Marat <maratenikeev@yandex.ru> - http://
 */

defined('_JEXEC') or die;

// Include dependancies
jimport('joomla.application.component.controller');

// Execute the task.
$controller	= JControllerLegacy::getInstance('Codeanalyzer');
$controller->execute(JFactory::getApplication()->input->get('task'));
$controller->redirect();
